package com.jonecx.qio

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.jonecx.qio.ui.theme.QioTheme
import com.jonecx.qio.ui.vm.LoginViewModel
import com.jonecx.qio.utils.OauthUtils.Companion.authorizeRequestUrl
import com.jonecx.qio.utils.extractAuthorizationCode
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AuthorizationScreen(loginViewModel::getAccessToken)
                }
            }
        }
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
