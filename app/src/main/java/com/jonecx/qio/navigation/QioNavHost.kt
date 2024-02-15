package com.jonecx.qio.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.jonecx.qio.feature.activity.navigation.activityScreen
import com.jonecx.qio.feature.home.HOME_ROUTE
import com.jonecx.qio.feature.home.homeScreen
import com.jonecx.qio.feature.profile.navigation.profileScreen
import com.jonecx.qio.feature.search.navigation.searchScreen
import com.jonecx.qio.ui.QioAppState

@Composable
fun QioNavHost(
    appState: QioAppState,
    onShowSnackbar: suspend (String, String) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen()
        searchScreen()
        activityScreen()
        profileScreen()
    }
}
