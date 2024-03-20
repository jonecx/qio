package com.jonecx.qio

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jonecx.qio.feature.appstate.AppSessionViewModel
import com.jonecx.qio.feature.appstate.AppState
import com.jonecx.qio.feature.appstate.AppState.AppStateLoading
import com.jonecx.qio.feature.appstate.AppState.AppStateSuccess
import com.jonecx.qio.feature.authentication.AuthorizationScreen
import com.jonecx.qio.feature.authentication.ErrorScreen
import com.jonecx.qio.feature.authentication.LoadingScreen
import com.jonecx.qio.feature.authentication.LoginState
import com.jonecx.qio.feature.authentication.LoginViewModel
import com.jonecx.qio.settings.proto.ThemeConfig
import com.jonecx.qio.ui.QioApp
import com.jonecx.qio.ui.theme.QioTheme
import com.jonecx.qio.utils.internet.InternetConnectionMonitor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var internetConnectionMonitor: InternetConnectionMonitor

    private val appSessionViewModel: AppSessionViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var appState: AppState by mutableStateOf(AppStateLoading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appSessionViewModel.appSessionState.collectLatest {
                    appState = it
                }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (appState) {
                AppStateLoading -> true
                is AppStateSuccess -> false
            }
        }

        setContent {
            val isUseDarkTheme = isUseDarkTheme(appState)
            val authenticationState by loginViewModel.authorizationState.collectAsStateWithLifecycle()

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
                        when (authenticationState) {
                            LoginState.LoginStateLoading -> loginViewModel.getCurrentAuthenticationState()
                            LoginState.LoginStateRefreshing -> LoadingScreen()
                            LoginState.LoginStateError -> ErrorScreen()
                            LoginState.LoginStateSuccess -> QioApp(
                                windowSizeClass = calculateWindowSizeClass(this),
                                networkMonitor = internetConnectionMonitor,
                            )
                            LoginState.LoginStateUnknown -> AuthorizationScreen(
                                loginViewModel::getAccessToken,
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
private fun isUseDarkTheme(appState: AppState) = when (appState) {
    AppStateLoading -> false
    is AppStateSuccess -> when (appState.userSettings.darkThemeConfig) {
        ThemeConfig.DARK -> true
        ThemeConfig.LIGHT -> false
        ThemeConfig.FOLLOW_SYSTEM,
        ThemeConfig.UNRECOGNIZED,
        null,
        -> isSystemInDarkTheme()
    }
}

@Composable
private fun isUseDynamicColor(appState: AppState) = when (appState) {
    AppStateLoading -> false
    is AppStateSuccess -> appState.userSettings.useDynamicColor ?: false
}
