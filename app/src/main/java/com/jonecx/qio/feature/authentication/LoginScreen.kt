package com.jonecx.qio.feature.authentication

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
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
