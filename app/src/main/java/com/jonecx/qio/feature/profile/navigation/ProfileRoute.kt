package com.jonecx.qio.feature.profile.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun ProfileRoute() {
    ProfileScreen()
}

@Composable
internal fun ProfileScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(text = "Authenticated profile", modifier = Modifier.align(Alignment.Center))
    }
}
