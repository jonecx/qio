package com.jonecx.qio.feature.search.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun SearchRoute() {
    SearchScreen()
}

@Composable
internal fun SearchScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Text(text = "Authenticated search", modifier = Modifier.align(Alignment.Center))
    }
}
