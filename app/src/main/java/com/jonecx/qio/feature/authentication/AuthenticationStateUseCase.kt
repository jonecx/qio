package com.jonecx.qio.feature.authentication

import android.content.SharedPreferences
import com.jonecx.qio.model.OauthTokenInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthenticationStateUseCase @Inject constructor(private val encryptedStorage: SharedPreferences) {
    operator fun invoke(): Flow<OauthTokenInfo> = flow {
        val oauthTokenInfo = encryptedStorage.getOauthTokenInfo()
        emit(oauthTokenInfo)
    }
}
