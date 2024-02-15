package com.jonecx.qio.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun HomeRoute() {
    HomeScreen()
}

@Composable
internal fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(text = "Authenticated home", modifier = Modifier.align(Alignment.Center))
    }
}
