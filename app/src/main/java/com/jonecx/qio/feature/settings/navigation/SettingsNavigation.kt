package com.jonecx.qio.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.jetbrains.annotations.VisibleForTesting

@VisibleForTesting
internal const val SETTINGS_ROUTE = "settings_route"

fun NavController.navigateToSettings() = navigate(SETTINGS_ROUTE)

fun NavGraphBuilder.settingsScreen() {
    composable(route = SETTINGS_ROUTE) {
        SettingsRoute()
    }
}
