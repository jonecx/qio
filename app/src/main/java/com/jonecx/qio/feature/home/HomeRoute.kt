package com.jonecx.qio.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jonecx.qio.ui.QioCircularProgress

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
        QioCircularProgress(modifier = Modifier.align(Alignment.Center))
    }
}
