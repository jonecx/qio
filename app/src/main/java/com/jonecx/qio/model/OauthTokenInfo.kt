package com.jonecx.qio.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OauthTokenInfo(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("token_type") val tokenType: String,
    val scope: String,
    @SerialName("id_token") val idToken: Boolean,
)

fun OauthTokenInfo.isValid(): Boolean {
    return accessToken.isNotBlank() && tokenType == "bearer" && expiresIn != 0
}
