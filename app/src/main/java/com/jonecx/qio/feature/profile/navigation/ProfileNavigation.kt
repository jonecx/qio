package com.jonecx.qio.feature.profile.navigation

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val PROFILE_ROUTE = "profile_route"

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(PROFILE_ROUTE, navOptions)

fun NavGraphBuilder.profileScreen() {
    composable(
        route = PROFILE_ROUTE,
    ) {
        val context = LocalContext.current
        val onEdit = {
            Toast.makeText(context, "Editing profile", Toast.LENGTH_SHORT).show()
        }
        val onShare = {
            Toast.makeText(context, "Sharing profile", Toast.LENGTH_SHORT).show()
        }
        val onSettings = {
            Toast.makeText(context, "Changing settings", Toast.LENGTH_SHORT).show()
        }
        ProfileRoute(onEdit, onShare, onSettings)
    }
}
