package com.jonecx.qio.ui.navbars

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination

@Composable
fun QioSideNavRail(
    destinations: List<TopLevelDestination>,
    destinationsWithUnreadResources: Set<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier,
) {
    SideNavRail(modifier = modifier) {
        destinations.forEach { destination ->
            val isSelected = currentDestination.isRouteTopInHierarchy(destination)
            val isUnread = destinationsWithUnreadResources.contains(destination)
            SideNavRailItem(
                isSelected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = stringResource(id = destination.titleTextId),
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.titleTextId),
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = if (isUnread) Modifier.notificationDot() else Modifier,
            )
        }
    }
}

@Composable
private fun SideNavRail(
    modifier: Modifier,
    header: @Composable (ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier.padding(top = 40.dp),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = QioNavigationDefaults.navigationContentColor(),
        header = header,
        content = content,
    )
}

@Composable
private fun SideNavRailItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    isEnabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    isAlwaysShowLabel: Boolean = true,
) {
    NavigationRailItem(
        selected = isSelected,
        onClick = onClick,
        icon = if (isSelected) selectedIcon else icon,
        modifier = modifier.padding(top = 20.dp),
        enabled = isEnabled,
        label = label,
        alwaysShowLabel = isAlwaysShowLabel,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = QioNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = QioNavigationDefaults.navigationContentColor(),
            selectedTextColor = QioNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = QioNavigationDefaults.navigationContentColor(),
            indicatorColor = QioNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}
