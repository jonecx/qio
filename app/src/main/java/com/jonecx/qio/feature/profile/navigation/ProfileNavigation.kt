package com.jonecx.qio.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val PROFILE_ROUTE = "profile_route"
const val PROFILE_GRAPH_ROUTE = "profile_graph"

fun NavController.navigateToProfile(navOptions: NavOptions) = navigate(PROFILE_ROUTE, navOptions)

fun NavGraphBuilder.profileScreen(
    onEdit: () -> Unit,
    onShare: () -> Unit,
    onSettings: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = PROFILE_GRAPH_ROUTE,
        startDestination = PROFILE_ROUTE,
    ) {
        composable(route = PROFILE_ROUTE) {
            ProfileRoute(onEdit, onShare, onSettings)
        }
        nestedGraphs()
    }
}
