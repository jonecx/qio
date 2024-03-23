package com.jonecx.qio.network

import android.content.SharedPreferences
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.feature.authentication.OauthUtils
import com.jonecx.qio.feature.profile.ProfileInfoState
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.ProfileInfo
import com.jonecx.qio.model.isValid
import com.jonecx.qio.network.ApiResult.Error
import com.jonecx.qio.network.ApiResult.Success
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

class ApiService @Inject constructor(private val encryptedStorage: SharedPreferences, private val httpClient: HttpClient) : IApiService {

    override fun authorize(authorizationCode: String): Flow<ApiResult<OauthTokenInfo>> = flow {
        try {
            val url = BuildConfig.AUTHORIZATION_CODE_GRANT_URL
            val response = httpClient.post(url) {
                val formData = OauthUtils.getAuthorizationRequestTokenParams(authorizationCode)
                setBody(formData)
            }
            val oauthTokenInfo = runBlocking { OauthUtils.handleOauthInfo(encryptedStorage, response) }
            when (oauthTokenInfo.isValid()) {
                true -> emit(Success(oauthTokenInfo))
                false -> emit(Error("Authorization failed!"))
            }
        } catch (e: Exception) {
            Timber.d(e.message, "Authorization failed!")
            emit(Error(e.message ?: "Authorization failed!"))
        }
    }

    override fun refreshToken(oauthTokenInfo: OauthTokenInfo): Flow<ApiResult<OauthTokenInfo>> = flow {
        try {
            val url = BuildConfig.AUTHORIZATION_CODE_GRANT_URL
            val response = httpClient.post(url) {
                val formData = OauthUtils.getRefreshTokenRequestFormData(oauthTokenInfo)
                setBody(formData)
            }
            val refreshedOauthTokenInfo = runBlocking { OauthUtils.handleOauthInfo(encryptedStorage, response) }
            when (oauthTokenInfo.isValid()) {
                true -> emit(Success(refreshedOauthTokenInfo))
                false -> emit(Error("Token refresh failed!"))
            }
        } catch (e: Exception) {
            Timber.d("Refreshing token failed! ${e.localizedMessage}")
            emit(Error(e.message ?: "Refreshing token failed!"))
        }
    }

    override fun getSelfProfile(): Flow<ProfileInfoState> = flow {
        try {
            val profileInfo: ProfileInfo = httpClient.get("/v2/user/info").body()
            emit(ProfileInfoState.Success(profileInfo))
        } catch (e: java.lang.Exception) {
            Timber.d("Getting user profile info failed! ${e.localizedMessage}")
            emit(ProfileInfoState.Error)
        }
    }
}
