package com.jonecx.qio.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonecx.qio.QioNavHost
import com.jonecx.qio.R
import com.jonecx.qio.ui.navbars.QioBottomNavBar
import com.jonecx.qio.ui.navbars.QioSideNavRail
import com.jonecx.qio.ui.navbars.QioTopAppBar
import com.jonecx.qio.ui.navbars.TopLevelDestination
import com.jonecx.qio.ui.theme.QioBackground
import com.jonecx.qio.ui.theme.QioGradientBackground
import com.jonecx.qio.utils.internet.NetworkMonitor

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun QioApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    qioAppState: QioAppState = rememberQioAppState(
        windowSizeClass = windowSizeClass,
        networkMonitor = networkMonitor,
    ),
) {
    val isShowGradientBackground =
        qioAppState.currentTopLevelDestination == TopLevelDestination.HOME

    QioBackground {
        QioGradientBackground(isShowGradientBackground = isShowGradientBackground) {
            val snackbarHostState = remember { SnackbarHostState() }

            val isOffline by qioAppState.isOffline.collectAsStateWithLifecycle()
            val noInternetMsg = stringResource(id = R.string.no_internet_connection)
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = noInternetMsg,
                        duration = Indefinite,
                    )
                }
            }

            val unreadDestinations by qioAppState.topLevelDestinationsWithUnreadResources.collectAsStateWithLifecycle()

            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (qioAppState.isShowBottomBar) {
                        QioBottomNavBar(
                            destinations = qioAppState.topLevelDestinations,
                            destinationsWithUnreadResources = unreadDestinations,
                            onNavigateToDestination = qioAppState::navigateToTopNavBar,
                            currentDestination = qioAppState.currentDestination,
                            modifier = Modifier.testTag("QioBottomNavBarTag"),
                        )
                    }
                },
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                        ),

                ) {
                    if (qioAppState.isShowNavRail) {
                        QioSideNavRail(
                            destinations = qioAppState.topLevelDestinations,
                            destinationsWithUnreadResources = unreadDestinations,
                            onNavigateToDestination = qioAppState::navigateToTopNavBar,
                            currentDestination = qioAppState.currentDestination,
                            modifier = Modifier
                                .safeDrawingPadding()
                                .testTag("QioSideNavRailTag"),
                        )
                    }

                    Column(Modifier.fillMaxSize()) {
                        qioAppState.currentTopLevelDestination?.let {
                            if (qioAppState.isShowBottomBar && it != TopLevelDestination.PROFILE) {
                                QioTopAppBar()
                            }
                        }

                        QioNavHost(appState = qioAppState, onShowSnackbar = { message, action ->
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = SnackbarDuration.Short,
                            ) == SnackbarResult.ActionPerformed
                        })
                    }
                }
            }
        }
    }
}
