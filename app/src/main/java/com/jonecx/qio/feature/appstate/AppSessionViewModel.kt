package com.jonecx.qio.feature.appstate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonecx.qio.settings.SettingsData
import com.jonecx.qio.settings.UserSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppSessionViewModel @Inject constructor(private val userSettingsRepository: UserSettingsRepository) : ViewModel() {

    val appSessionState: StateFlow<AppState> = flow<AppState> {
        val userSettingsData = getUserSettings()
        emit(AppState.AppStateSuccess(userSettingsData))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppState.AppStateLoading,
    )

    private suspend fun getUserSettings(): SettingsData {
        return userSettingsRepository.settingsData.first()
    }
}

sealed interface AppState {
    data object AppStateLoading : AppState
    data class AppStateSuccess(val userSettings: SettingsData) : AppState
}
