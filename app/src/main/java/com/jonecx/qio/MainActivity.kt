package com.jonecx.qio

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import com.jonecx.qio.feature.authentication.AuthorizationScreen
import com.jonecx.qio.feature.authentication.ErrorScreen
import com.jonecx.qio.feature.authentication.LoginViewModel
import com.jonecx.qio.network.ApiResult
import com.jonecx.qio.network.ApiResult.Error
import com.jonecx.qio.network.ApiResult.Loading
import com.jonecx.qio.network.ApiResult.Success
import com.jonecx.qio.settings.proto.ThemeConfig
import com.jonecx.qio.settings.proto.userSettings
import com.jonecx.qio.ui.QioApp
import com.jonecx.qio.ui.theme.QioTheme
import com.jonecx.qio.utils.internet.InternetConnectionMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var internetConnectionMonitor: InternetConnectionMonitor

    private val appSessionViewModel: AppSessionViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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

        setContent {
            val isUseDarkTheme = isUseDarkTheme(sessionState = sessionState)

            DisposableEffect(key1 = isUseDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { isUseDarkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ),
                )
                onDispose { }
            }

            CompositionLocalProvider {
                QioTheme(darkTheme = isUseDarkTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        val needsOnBoarding = when (sessionState) {
                            SessionStateLoading -> { /* NOOP */ }
                            is SessionStateValue -> {
                                when {
                                    (sessionState as SessionStateValue).isUserAuthenticated || authenticationState is Success -> null
                                    authenticationState is Error -> ErrorScreen()
                                    authenticationState is Loading -> AuthorizationScreen(
                                        loginViewModel::getAccessToken,
                                    )
                                    else -> { /* NOOP */ }
                                }
                            }
                        }
                        if (needsOnBoarding == null) {
                            QioApp(
                                windowSizeClass = calculateWindowSizeClass(this),
                                networkMonitor = internetConnectionMonitor,
                            )
                        }
                    }
                }
            }
        }
    }
}

private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

@Composable
private fun isUseDarkTheme(sessionState: SessionState) = when (sessionState) {
    SessionStateLoading -> false
    is SessionStateValue -> when (sessionState.userSettings?.darkThemeConfig) {
        ThemeConfig.DARK -> true
        ThemeConfig.LIGHT -> false
        ThemeConfig.FOLLOW_SYSTEM,
        ThemeConfig.UNRECOGNIZED,
        null,
        -> isSystemInDarkTheme()
    }
}

@Composable
private fun isUseDynamicColor(sessionState: SessionState) = when (sessionState) {
    SessionStateLoading -> false
    is SessionStateValue -> sessionState.userSettings?.useDynamicColor ?: false
}
