package com.jonecx.qio.network

import android.content.SharedPreferences
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.feature.authentication.OauthUtils
import com.jonecx.qio.network.ApiResult.Error
import com.jonecx.qio.network.ApiResult.Loading
import com.jonecx.qio.network.ApiResult.Success
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ApiService @Inject constructor(private val encryptedStorage: SharedPreferences, private val httpClient: HttpClient) : IApiService {

    override fun authorize(authorizationCode: String): Flow<ApiResult<Boolean>> = flow {
        emit(Loading())
        try {
            val url = BuildConfig.AUTHORIZATION_CODE_GRANT_URL
            val response = httpClient.post(url) {
                val formData = OauthUtils.getRequestTokenParams(authorizationCode)
                setBody(formData)
            }
            val isRequestSuccessful = response.status.value == 200
            if (isRequestSuccessful) {
                with(encryptedStorage.edit()) {
                    putString("token", response.bodyAsText())
                    apply()
                }
            }
            emit(Success(isRequestSuccessful))
        } catch (e: Exception) {
            Timber.e(e)
            emit(Error(e.message ?: "Authorization failed!"))
        }
    }
}
