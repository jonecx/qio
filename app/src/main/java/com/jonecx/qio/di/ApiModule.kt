package com.jonecx.qio.di

import android.content.SharedPreferences
import com.jonecx.qio.BuildConfig
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
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

        install(DefaultRequest) {
            url(BuildConfig.BASE_URL)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            if (!headers.contains("No-Authentication")) {
                val tokenInfo = encryptedStorage.getString("token", "")
                if (tokenInfo?.isNotBlank() == true) {
                    val oauthTokenInfo = Json.decodeFromString<OauthTokenInfo>(tokenInfo)
                    header("Authorization", "Bearer ${oauthTokenInfo.accessToken}")
                }
            }
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
