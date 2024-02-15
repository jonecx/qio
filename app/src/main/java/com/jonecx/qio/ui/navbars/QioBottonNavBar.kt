package com.jonecx.qio.ui.navbars

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination

@Composable
fun QioBottomNavBar(
    destinations: List<TopLevelDestination>,
    destinationsWithUnreadResources: Set<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    BottomNavBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val hasUnread = destinationsWithUnreadResources.contains(destination)
            val isSelected = currentDestination.isRouteTopInHierarchy(destination)
            BottomNavBarItem(
                isSelected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(id = destination.iconTextId)) },
                modifier = if (hasUnread) Modifier.notificationDot() else Modifier,
            )
        }
    }
}

@Composable
private fun BottomNavBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = QioNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content,
    )
}

@Composable
private fun RowScope.BottomNavBarItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    isEnabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    isShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = if (isSelected) selectedIcon else icon,
        modifier = modifier,
        enabled = isEnabled,
        label = label,
        alwaysShowLabel = isShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = QioNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = QioNavigationDefaults.navigationContentColor(),
            selectedTextColor = QioNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = QioNavigationDefaults.navigationContentColor(),
            indicatorColor = QioNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}
