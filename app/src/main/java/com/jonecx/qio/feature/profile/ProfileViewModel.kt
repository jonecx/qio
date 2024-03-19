package com.jonecx.qio.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonecx.qio.model.ProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInfoUseCase: ProfileInfoUseCase,
) : ViewModel() {
    val profileState = profileInfoUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileInfoState.Loading,
    )
}

sealed interface ProfileInfoState {

    data class Success(val profileInfo: ProfileInfo) : ProfileInfoState
    data object Error : ProfileInfoState
    data object Loading : ProfileInfoState
}
