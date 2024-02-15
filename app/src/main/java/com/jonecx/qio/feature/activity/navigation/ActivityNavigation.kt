package com.jonecx.qio.feature.activity.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ACTIVITY_ROUTE = "activity_route"

fun NavController.navigateToActivity(navOptions: NavOptions) = navigate(ACTIVITY_ROUTE, navOptions)

fun NavGraphBuilder.activityScreen() {
    composable(
        route = ACTIVITY_ROUTE,
    ) {
        ActivityRoute()
    }
}
