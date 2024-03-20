package com.jonecx.qio.feature.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jonecx.qio.ui.QioCircularProgress
import com.jonecx.qio.ui.theme.QioBackground
import com.jonecx.qio.ui.theme.QioTheme
import previews.DevicePreviewProfiles

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        QioCircularProgress(size = 90.dp)
    }
}

@DevicePreviewProfiles
@Composable
fun LoadingScreenPreview() {
    QioTheme {
        QioBackground {
            LoadingScreen()
        }
    }
}
