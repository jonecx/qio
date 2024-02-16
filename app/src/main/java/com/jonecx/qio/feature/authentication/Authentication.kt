package com.jonecx.qio.feature.authentication

import android.content.res.Configuration
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import timber.log.Timber

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
                        Timber.d("Authorization code retrieved")
                        authorizeAccess(authorizationCode)
                        return true
                    }
                    return false
                }
            }
            Timber.d("Start authorization steps")
            loadUrl(OauthUtils.authorizeRequestUrl)
        }
    }, update = {
        it.loadUrl(OauthUtils.authorizeRequestUrl)
    })
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
        contentAlignment = Alignment.Center,
    ) {
        Text(":( failure")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen()
}
