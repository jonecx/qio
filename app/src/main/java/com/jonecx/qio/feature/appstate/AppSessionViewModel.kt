package com.jonecx.qio.feature.appstate

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonecx.qio.feature.appstate.SessionState.SessionStateValue
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.isValid
import com.jonecx.qio.utils.orBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppSessionViewModel @Inject constructor(private val encryptedStorageModel: SharedPreferences) : ViewModel() {

    val sessionState: StateFlow<SessionState> = flow<SessionState> {
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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionState.SessionStateLoading,
    )
}

sealed interface SessionState {
    data object SessionStateLoading : SessionState
    data class SessionStateValue(val isUserAuthenticated: Boolean) : SessionState
}
