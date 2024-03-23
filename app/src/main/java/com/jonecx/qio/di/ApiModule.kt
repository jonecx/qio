package com.jonecx.qio.di

import android.content.SharedPreferences
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.feature.authentication.OauthUtils
import com.jonecx.qio.feature.authentication.getOauthTokenInfo
import com.jonecx.qio.model.isTokenExpired
import com.jonecx.qio.model.isValid
import com.jonecx.qio.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun providesHttpClient(encryptedStorage: SharedPreferences) = HttpClient(Android) {
        install(Logging) {
            level = ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val oauthTokenInfo = encryptedStorage.getOauthTokenInfo()
                    if (oauthTokenInfo.isValid() && !oauthTokenInfo.isTokenExpired()) {
                        BearerTokens(oauthTokenInfo.accessToken, oauthTokenInfo.refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val oauthTokenInfo = encryptedStorage.getOauthTokenInfo()
                    val freshOauthTokenInfo = if (oauthTokenInfo.isValid() && !oauthTokenInfo.isTokenExpired()) {
                        oauthTokenInfo
                    } else {
                        val tokenRefreshResponse = client.submitForm(
                            url = BuildConfig.AUTHORIZATION_CODE_GRANT_URL,
                            formParameters = OauthUtils.getRefreshTokenRequestParameters(
                                oauthTokenInfo,
                            ),
                        ) { markAsRefreshTokenRequest() }
                        runBlocking {
                            OauthUtils.handleOauthInfo(encryptedStorage, tokenRefreshResponse)
                        }
                    }

                    BearerTokens(
                        freshOauthTokenInfo.accessToken,
                        freshOauthTokenInfo.refreshToken,
                    )
                }
            }
        }

        install(DefaultRequest) {
            url(BuildConfig.BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }

    @Singleton
    @Provides
    fun providesApiService(encryptedStorage: SharedPreferences, httpClient: HttpClient): ApiService = ApiService(encryptedStorage, httpClient)
}
