package com.jonecx.qio.feature.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.jonecx.qio.ui.theme.QioBackground
import com.jonecx.qio.ui.theme.QioTheme
import previews.DevicePreviewProfiles

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier
            .testTag("qio_error_screen")
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.error),
        contentAlignment = Alignment.Center,
    ) {
        Text(":( failure")
    }
}

@DevicePreviewProfiles
@Composable
fun ErrorScreenPreview() {
    QioTheme {
        QioBackground {
            ErrorScreen()
        }
    }
}
