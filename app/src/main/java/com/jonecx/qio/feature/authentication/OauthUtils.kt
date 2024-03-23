package com.jonecx.qio.feature.authentication

import android.content.SharedPreferences
import android.webkit.WebResourceRequest
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.feature.authentication.OauthUtils.Companion.AUTHORIZATION_CODE_PARAM
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.isValid
import com.jonecx.qio.model.serialize
import com.jonecx.qio.utils.getRandomString
import com.jonecx.qio.utils.orBlank
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.Instant

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
        fun getRefreshTokenRequestFormData(oauthTokenInfo: OauthTokenInfo) = FormDataContent(
            getRefreshTokenRequestParameters(oauthTokenInfo),
        )

        fun getRefreshTokenRequestParameters(oauthTokenInfo: OauthTokenInfo) = Parameters.build {
            append("grant_type", BuildConfig.GRANT_TYPE_REFRESH_TOKEN)
            append("client_id", BuildConfig.CLIENT_ID)
            append("client_secret", BuildConfig.CLIENT_SECRET)
            append("refresh_token", oauthTokenInfo.refreshToken)
        }

        fun deserializeOauthInfo(oauthJson: String): OauthTokenInfo {
            return runCatching {
                Json.decodeFromString<OauthTokenInfo>(oauthJson)
            }.getOrElse { OauthTokenInfo() }
        }

        suspend fun handleOauthInfo(
            encryptedStorage: SharedPreferences,
            httpResponse: HttpResponse,
        ): OauthTokenInfo {
            val oauthInfoJson = httpResponse.bodyAsText()
            val oauthTokenInfo = deserializeOauthInfo(oauthInfoJson).apply {
                if (isValid()) {
                    // update the token expiry time
                    val currentTime: Long = Instant.now().epochSecond
                    val expiryTime = currentTime + expiresIn
                    expiresIn = expiryTime
                }
            }
            with(encryptedStorage.edit()) {
                val serializedOauthTokenInfo = oauthTokenInfo.serialize()
                putString("token", serializedOauthTokenInfo)
                commit()
            }
            return oauthTokenInfo
        }
    }
}

fun SharedPreferences.getOauthTokenInfo(): OauthTokenInfo {
    val oauthTokenInfoInJson = getString("token", "") ?: ""
    return OauthUtils.deserializeOauthInfo(oauthTokenInfoInJson)
}

fun WebResourceRequest.extractAuthorizationCode(): String {
    return when (this.url.toString().startsWith(BuildConfig.REDIRECT_URL_WITH_CODE)) {
        true -> url.getQueryParameter(AUTHORIZATION_CODE_PARAM).orBlank()
        false -> ""
    }
}
