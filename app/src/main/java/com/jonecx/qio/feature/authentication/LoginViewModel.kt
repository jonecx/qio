package com.jonecx.qio.feature.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonecx.qio.di.Dispatcher
import com.jonecx.qio.di.QioDispatchers
import com.jonecx.qio.network.ApiResult
import com.jonecx.qio.network.ApiResult.Loading
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
) : ViewModel() {

    private val _authorizationState = MutableStateFlow<ApiResult<Boolean>>(Loading())
    val authorizationState = _authorizationState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading(),
    )

    fun getAccessToken(authorizationCode: String) {
        viewModelScope.launch(ioDispatcher) {
            authorizeUseCase(authorizationCode).collectLatest {
                _authorizationState.value = it
            }
        }
    }
}
