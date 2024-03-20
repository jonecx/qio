package com.jonecx.qio.feature.authentication

import android.webkit.WebResourceRequest
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.feature.authentication.OauthUtils.Companion.AUTHORIZATION_CODE_PARAM
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.utils.getRandomString
import com.jonecx.qio.utils.orBlank
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters

class OauthUtils {
    companion object {

        const val AUTHORIZATION_CODE_PARAM: String = "code"

        val authorizeRequestUrl: String = buildString {
            append(BuildConfig.AUTHORIZE_URL)
            append("?client_id=").append(BuildConfig.CLIENT_ID)
            append("&response_type=").append(BuildConfig.RESPONSE_TYPE)
            append("&scope=").append(BuildConfig.SCOPE)
            append("&state=").append(getRandomString(20))
            append("&redirect_uri=").append(BuildConfig.REDIRECT_URI)
        }

        fun getAuthorizationRequestTokenParams(authorizationCode: String) = FormDataContent(
            Parameters.build {
                append("grant_type", BuildConfig.GRANT_TYPE_AUTHORIZATION)
                append(AUTHORIZATION_CODE_PARAM, authorizationCode)
                append("client_id", BuildConfig.CLIENT_ID)
                append("client_secret", BuildConfig.CLIENT_SECRET)
                append("redirect_uri", BuildConfig.REDIRECT_URI)
            },
        )
        fun getRefreshTokenRequestTokenParams(oauthTokenInfo: OauthTokenInfo) = FormDataContent(
            Parameters.build {
                append("grant_type", BuildConfig.GRANT_TYPE_REFRESH_TOKEN)
                append("client_id", BuildConfig.CLIENT_ID)
                append("client_secret", BuildConfig.CLIENT_SECRET)
                append("refresh_token", oauthTokenInfo.refreshToken)
            },
        )
    }
}

fun WebResourceRequest.extractAuthorizationCode(): String {
    return when (this.url.toString().startsWith(BuildConfig.REDIRECT_URL_WITH_CODE)) {
        true -> url.getQueryParameter(AUTHORIZATION_CODE_PARAM).orBlank()
        false -> ""
    }
}
