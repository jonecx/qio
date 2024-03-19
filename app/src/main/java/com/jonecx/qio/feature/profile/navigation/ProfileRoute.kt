package com.jonecx.qio.feature.profile.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonecx.qio.feature.profile.ProfileInfoState
import com.jonecx.qio.feature.profile.ProfileViewModel
import com.jonecx.qio.feature.profile.screen.ProfileScreen

@Composable
internal fun ProfileRoute(onEdit: () -> Unit, onShare: () -> Unit, onSettings: () -> Unit, profileViewModel: ProfileViewModel = hiltViewModel()) {
    val profileInfoState: ProfileInfoState by profileViewModel.profileState.collectAsStateWithLifecycle()
    ProfileScreen(profileInfoState, onEdit, onShare, onSettings)
}
