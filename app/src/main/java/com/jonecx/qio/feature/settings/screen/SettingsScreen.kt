package com.jonecx.qio.feature.settings.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jonecx.qio.ui.theme.QioBackground
import com.jonecx.qio.ui.theme.QioTheme
import previews.DevicePreviewProfiles

@Composable
internal fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Yellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Thanks")
    }
}

@Composable
@DevicePreviewProfiles
@Preview
fun SettingsScreenPreview() {
    QioTheme {
        QioBackground {
            SettingsScreen()
        }
    }
}
