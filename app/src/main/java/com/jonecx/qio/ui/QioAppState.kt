package com.jonecx.qio.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.jonecx.qio.feature.activity.navigation.ACTIVITY_ROUTE
import com.jonecx.qio.feature.activity.navigation.navigateToActivity
import com.jonecx.qio.feature.home.HOME_ROUTE
import com.jonecx.qio.feature.home.navigateToHome
import com.jonecx.qio.feature.profile.navigation.PROFILE_ROUTE
import com.jonecx.qio.feature.profile.navigation.navigateToProfile
import com.jonecx.qio.feature.search.navigation.SEARCH_ROUTE
import com.jonecx.qio.feature.search.navigation.navigateToSearch
import com.jonecx.qio.ui.navbars.TopLevelDestination
import com.jonecx.qio.ui.navbars.TopLevelDestination.ACTIVITY
import com.jonecx.qio.ui.navbars.TopLevelDestination.HOME
import com.jonecx.qio.ui.navbars.TopLevelDestination.PROFILE
import com.jonecx.qio.ui.navbars.TopLevelDestination.SEARCH
import com.jonecx.qio.utils.TrackDisposableJank
import com.jonecx.qio.utils.internet.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

@Composable
fun rememberQioAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): QioAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
    ) {
        QioAppState(
            navController,
            coroutineScope,
            windowSizeClass,
            networkMonitor,
        )
    }
}

class QioAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            HOME_ROUTE -> HOME
            SEARCH_ROUTE -> SEARCH
            ACTIVITY_ROUTE -> ACTIVITY
            PROFILE_ROUTE -> PROFILE
            else -> null
        }

    val isShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val isShowNavRail: Boolean
        get() = !isShowBottomBar

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> = runBlocking {
        val mutableStateFlow = MutableStateFlow(emptySet<TopLevelDestination>())
        mutableStateFlow.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet(),
        )
    }

    fun navigateToTopNavBar(bottomNavDestinations: TopLevelDestination) {
        trace("Bottom Navigation: ${bottomNavDestinations.name}") {
            val bottomNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            when (bottomNavDestinations) {
                HOME -> navController.navigateToHome(bottomNavOptions)
                SEARCH -> navController.navigateToSearch(bottomNavOptions)
                ACTIVITY -> navController.navigateToActivity(bottomNavOptions)
                PROFILE -> navController.navigateToProfile(bottomNavOptions)
            }
        }
    }
}

@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
