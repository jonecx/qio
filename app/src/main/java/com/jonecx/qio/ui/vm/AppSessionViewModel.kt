package com.jonecx.qio.ui.vm

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.isValid
import com.jonecx.qio.ui.vm.SessionState.SessionStateValue
import com.jonecx.qio.utils.orBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppSessionViewModel @Inject constructor(private val encryptedStorageModel: SharedPreferences) : ViewModel() {

    val sessionState: Flow<SessionState> = flow<SessionState> {
        try {
            val tokenInfo = encryptedStorageModel.getString("token", "").orBlank()
            val oauthTokenInfo = Json.decodeFromString<OauthTokenInfo>(tokenInfo)
            when (oauthTokenInfo.isValid()) {
                true -> emit(SessionStateValue(true))
                false -> emit(SessionStateValue(false))
            }
        } catch (e: Exception) {
            Timber.e("Checking session status failed!")
            emit(SessionStateValue(false))
        }
    }
}

sealed interface SessionState {
    data object SessionStateLoading : SessionState
    data class SessionStateValue(val isUserAuthenticated: Boolean) : SessionState
}
