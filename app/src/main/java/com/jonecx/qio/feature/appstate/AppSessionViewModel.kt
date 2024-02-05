package com.jonecx.qio.feature.appstate

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonecx.qio.feature.appstate.SessionState.SessionStateValue
import com.jonecx.qio.model.OauthTokenInfo
import com.jonecx.qio.model.isValid
import com.jonecx.qio.settings.SettingsData
import com.jonecx.qio.settings.UserSettingsRepository
import com.jonecx.qio.utils.orBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppSessionViewModel @Inject constructor(private val encryptedStorageModel: SharedPreferences, private val userSettingsRepository: UserSettingsRepository) : ViewModel() {

    val sessionState: StateFlow<SessionState> = flow<SessionState> {
        try {
            val oauthTokenInfo = getOauthTokenInfo()
            val userSettingsData = getUserSettings()
            emit(SessionStateValue(oauthTokenInfo.isValid(), userSettingsData))
        } catch (e: Exception) {
            Timber.e("Checking session status failed!")
            emit(SessionStateValue(false, null))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionState.SessionStateLoading,
    )

    private suspend fun getOauthTokenInfo(): OauthTokenInfo {
        val tokenInfo = encryptedStorageModel.getString("token", "").orBlank()
        return Json.decodeFromString<OauthTokenInfo>(tokenInfo)
    }

    private suspend fun getUserSettings(): SettingsData {
        return userSettingsRepository.settingsData.first()
    }
}

sealed interface SessionState {
    data object SessionStateLoading : SessionState
    data class SessionStateValue(val isUserAuthenticated: Boolean, val userSettings: SettingsData?) : SessionState
}
