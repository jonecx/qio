package com.jonecx.qio.feature.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonecx.qio.di.Dispatcher
import com.jonecx.qio.di.QioDispatchers
import com.jonecx.qio.feature.authentication.LoginState.LoginStateError
import com.jonecx.qio.feature.authentication.LoginState.LoginStateLoading
import com.jonecx.qio.feature.authentication.LoginState.LoginStateRefreshing
import com.jonecx.qio.feature.authentication.LoginState.LoginStateSuccess
import com.jonecx.qio.feature.authentication.LoginState.LoginStateUnknown
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.isTokenExpired
import com.jonecx.qio.model.isValid
import com.jonecx.qio.network.ApiResult
import com.jonecx.qio.network.ApiResult.Error
import com.jonecx.qio.network.ApiResult.Loading
import com.jonecx.qio.network.ApiResult.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Dispatcher(QioDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val authorizeUseCase: AuthorizeUseCase,
    private val authenticationStateUseCase: AuthenticationStateUseCase,
    private val authenticationRefreshUseCase: AuthenticationRefreshUseCase,
) : ViewModel() {

    private val _authorizationState = MutableStateFlow<LoginState>(LoginStateLoading)
    val authorizationState = _authorizationState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LoginStateLoading,
    )

    fun getAccessToken(authorizationCode: String) {
        viewModelScope.launch(ioDispatcher) {
            authorizeUseCase(authorizationCode).collectLatest {
                postAuthResult(it)
            }
        }
    }

    private fun refreshToken(oauthTokenInfo: OauthTokenInfo) {
        viewModelScope.launch(ioDispatcher) {
            authenticationRefreshUseCase(oauthTokenInfo).collectLatest {
                postAuthResult(it)
            }
        }
    }

    fun getCurrentAuthenticationState() {
        viewModelScope.launch(ioDispatcher) {
            authenticationStateUseCase().collectLatest {
                when (it.isValid()) {
                    true -> when (it.isTokenExpired()) {
                        true -> {
                            refreshToken(oauthTokenInfo = it)
                            _authorizationState.value = LoginStateRefreshing
                        }
                        false -> _authorizationState.value = LoginStateSuccess
                    }
                    false -> _authorizationState.value = LoginStateUnknown
                }
            }
        }
    }

    private fun postAuthResult(result: ApiResult<OauthTokenInfo>) {
        when (result) {
            is Success -> _authorizationState.value = LoginStateSuccess
            is Error -> _authorizationState.value = LoginStateError
            is Loading -> {} // we do not want this sent over to Ui components
        }
    }
}

sealed interface LoginState {
    data object LoginStateLoading : LoginState
    data object LoginStateRefreshing : LoginState
    data object LoginStateError : LoginState
    data object LoginStateUnknown : LoginState
    data object LoginStateSuccess : LoginState
}
