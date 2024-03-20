package com.jonecx.qio.network

import android.content.SharedPreferences
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.feature.authentication.OauthUtils
import com.jonecx.qio.feature.profile.ProfileInfoState
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.ProfileInfo
import com.jonecx.qio.network.ApiResult.Error
import com.jonecx.qio.network.ApiResult.Loading
import com.jonecx.qio.network.ApiResult.Success
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

class ApiService @Inject constructor(private val encryptedStorage: SharedPreferences, private val httpClient: HttpClient) : IApiService {

    override fun authorize(authorizationCode: String): Flow<ApiResult<OauthTokenInfo>> = flow {
        try {
            val url = BuildConfig.AUTHORIZATION_CODE_GRANT_URL
            val response = httpClient.post(url) {
                val formData = OauthUtils.getAuthorizationRequestTokenParams(authorizationCode)
                setBody(formData)
            }
            val result = handleAuthenticationResult(response)
            emit(result)
        } catch (e: Exception) {
            Timber.e(e.message)
            emit(Error(e.message ?: "Authorization failed!"))
        }
    }

    override fun refreshToken(oauthTokenInfo: OauthTokenInfo): Flow<ApiResult<OauthTokenInfo>> = flow {
        try {
            val url = BuildConfig.AUTHORIZATION_CODE_GRANT_URL
            val response = httpClient.post(url) {
                val formData = OauthUtils.getRefreshTokenRequestTokenParams(oauthTokenInfo)
                setBody(formData)
            }
            val result = handleAuthenticationResult(response)
            emit(result)
        } catch (e: Exception) {
            Timber.e(e)
            emit(Error(e.message ?: "Refreshing token failed!"))
        }
    }

    override fun getSelfProfile(): Flow<ProfileInfoState> = flow {
        emit(ProfileInfoState.Loading)
        try {
            val profileInfo: ProfileInfo = httpClient.get("/v2/user/info").body()
            emit(ProfileInfoState.Success(profileInfo))
        } catch (e: java.lang.Exception) {
            emit(ProfileInfoState.Error)
        }
    }

    private suspend fun handleAuthenticationResult(httpResponse: HttpResponse): ApiResult<OauthTokenInfo> {
        return if (httpResponse.status.value == 200) {
            val oauthInfoJson = httpResponse.bodyAsText()
            val refreshedOauthTokenInfo = Json.decodeFromString<OauthTokenInfo>(oauthInfoJson).also {
                // update the token expiry time
                it.expiresIn += Instant.now().epochSecond
            }
            saveAuthenticationState(refreshedOauthTokenInfo)
            Success(refreshedOauthTokenInfo)
        } else {
            Error("Authorization failed!")
        }
    }

    private fun saveAuthenticationState(oauthTokenInfo: OauthTokenInfo) {
        with(encryptedStorage.edit()) {
            putString("token", Json.encodeToString(oauthTokenInfo))
            apply()
        }
    }
}
