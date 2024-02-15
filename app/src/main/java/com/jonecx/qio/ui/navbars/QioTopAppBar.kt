package com.jonecx.qio.ui.navbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QioTopAppBar(
    navigationIcon: ImageVector = Icons.Filled.Person,
    navigationIconContentDescription: String = "",
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors,
        modifier = modifier.testTag("QioTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun QioTopAppBarPreview() {
    QioTopAppBar(
        navigationIcon = Icons.Filled.Person,
        navigationIconContentDescription = "Navigation icon",
    )
}
