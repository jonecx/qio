package com.jonecx.qio.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

@Serializable
data class OauthTokenInfo(
    @SerialName("access_token") val accessToken: String = "",
    @SerialName("expires_in") var expiresIn: Long = 0,
    @SerialName("token_type") val tokenType: String = "",
    val scope: String = "",
    @SerialName("id_token") val idToken: Boolean = false,
    @SerialName("refresh_token") val refreshToken: String = "",
)

fun OauthTokenInfo.isValid(): Boolean {
    return accessToken.isNotBlank() && tokenType == "bearer"
}

fun OauthTokenInfo.isTokenExpired(): Boolean {
    val currentTimeInSeconds = Instant.now().epochSecond
    return currentTimeInSeconds > expiresIn
}

fun OauthTokenInfo.serialize() = Json.encodeToString(this)
