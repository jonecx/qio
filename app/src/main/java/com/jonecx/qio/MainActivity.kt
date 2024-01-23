package com.jonecx.qio

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jonecx.qio.feature.appstate.AppSessionViewModel
import com.jonecx.qio.feature.appstate.SessionState
import com.jonecx.qio.feature.appstate.SessionState.SessionStateLoading
import com.jonecx.qio.feature.appstate.SessionState.SessionStateValue
import com.jonecx.qio.feature.authentication.AuthenticatedScreen
import com.jonecx.qio.feature.authentication.AuthorizationScreen
import com.jonecx.qio.feature.authentication.ErrorScreen
import com.jonecx.qio.feature.authentication.LoginViewModel
import com.jonecx.qio.network.ApiResult
import com.jonecx.qio.network.ApiResult.Error
import com.jonecx.qio.network.ApiResult.Loading
import com.jonecx.qio.network.ApiResult.Success
import com.jonecx.qio.ui.theme.QioTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appSessionViewModel: AppSessionViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var sessionState: SessionState by mutableStateOf(SessionStateLoading)
        var authenticationState: ApiResult<Boolean> by mutableStateOf(Loading())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(appSessionViewModel.sessionState, loginViewModel.authorizationState) { currentSessionState, currentAuthorizationState ->
                    sessionState = currentSessionState
                    authenticationState = currentAuthorizationState
                }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (sessionState) {
                SessionStateLoading -> true
                is SessionStateValue -> false
            }
        }

        enableEdgeToEdge()
        setContent {
            QioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    when (sessionState) {
                        SessionStateLoading -> {
                            // NOOP
                            // Splash screen is still on
                        }
                        is SessionStateValue -> {
                            when ((sessionState as SessionStateValue).isUserAuthenticated) {
                                true -> AuthenticatedScreen()
                                false -> {
                                    when (authenticationState) {
                                        is Error -> ErrorScreen()
                                        is Loading -> AuthorizationScreen(loginViewModel::getAccessToken)
                                        is Success -> AuthenticatedScreen() // establish qioApp here
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
