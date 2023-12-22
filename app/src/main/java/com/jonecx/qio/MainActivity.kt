package com.jonecx.qio

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jonecx.qio.ui.theme.QioTheme
import com.jonecx.qio.ui.vm.AppSessionViewModel
import com.jonecx.qio.ui.vm.LoginViewModel
import com.jonecx.qio.ui.vm.SessionState
import com.jonecx.qio.ui.vm.SessionState.SessionStateLoading
import com.jonecx.qio.ui.vm.SessionState.SessionStateValue
import com.jonecx.qio.utils.OauthUtils.Companion.authorizeRequestUrl
import com.jonecx.qio.utils.extractAuthorizationCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val appSessionViewModel: AppSessionViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var sessionState: SessionState by mutableStateOf(SessionStateLoading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appSessionViewModel.sessionState
                    .onEach {
                        sessionState = it
                    }
                    .collect()
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
                    val isAuthenticated = (sessionState as SessionStateValue).isUserAuthenticated
                    when (isAuthenticated) {
                        true -> AuthenticatedScreen()
                        false -> AuthorizationScreen(loginViewModel::getAccessToken)
                    }
                }
            }
        }
    }
}

@Composable
fun AuthenticatedScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Text("Authenticated")
    }
}

@Composable
fun AuthorizationScreen(authorizeAccess: (String) -> Unit) {
    AndroidView(factory = {
        WebView(it).apply {
            this.settings.javaScriptEnabled = true
            this.settings.useWideViewPort = true
            this.settings.domStorageEnabled = true
            this.settings.allowContentAccess = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(webView: WebView, request: WebResourceRequest): Boolean {
                    val authorizationCode = request.extractAuthorizationCode()
                    if (authorizationCode.isNotBlank()) {
                        Timber.i("Authorization code retrieved")
                        authorizeAccess(authorizationCode)
                        return true
                    }
                    return false
                }
            }
            Timber.i("Start authorization steps")
            loadUrl(authorizeRequestUrl)
        }
    }, update = {
        it.loadUrl(authorizeRequestUrl)
    })
}
