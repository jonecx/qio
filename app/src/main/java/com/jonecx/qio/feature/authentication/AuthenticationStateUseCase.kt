package com.jonecx.qio.feature.authentication

import android.content.SharedPreferences
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.utils.orBlank
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthenticationStateUseCase @Inject constructor(private val encryptedStorageModel: SharedPreferences) {
    operator fun invoke(): Flow<OauthTokenInfo> = flow {
        val oauthTokenInfo = getOauthTokenInfo()
        emit(oauthTokenInfo)
    }

    private fun getOauthTokenInfo(): OauthTokenInfo {
        return try {
            val tokenInfo = encryptedStorageModel.getString("token", "").orBlank()
            return Json.decodeFromString<OauthTokenInfo>(tokenInfo)
        } catch (e: Exception) {
            OauthTokenInfo()
        }
    }
}
