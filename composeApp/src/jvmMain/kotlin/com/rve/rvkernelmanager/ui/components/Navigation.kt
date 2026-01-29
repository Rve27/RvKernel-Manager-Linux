package com.rve.rvkernelmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarArrangement
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Home
import com.composables.icons.materialsymbols.rounded.Lists
import com.composables.icons.materialsymbols.rounded.Memory
import com.composables.icons.materialsymbols.roundedfilled.Home
import com.composables.icons.materialsymbols.roundedfilled.Lists
import com.composables.icons.materialsymbols.roundedfilled.Memory
import com.rve.rvkernelmanager.CPU
import com.rve.rvkernelmanager.Home
import com.rve.rvkernelmanager.Kernel

object Navigation {
    data class NavItem(
        val label: String,
        val route: Any,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector
    )

    @Composable
    fun BottomNavigationBar(navController: NavController) {
        val items = listOf(
            NavItem(
                label = "Home",
                route = Home,
                selectedIcon = MaterialSymbols.RoundedFilled.Home,
                unselectedIcon = MaterialSymbols.Rounded.Home
            ),
            NavItem(
                label = "CPU",
                route = CPU,
                selectedIcon = MaterialSymbols.RoundedFilled.Memory,
                unselectedIcon = MaterialSymbols.Rounded.Memory
            ),
            NavItem(
                label = "Kernel",
                route = Kernel,
                selectedIcon = MaterialSymbols.RoundedFilled.Lists,
                unselectedIcon = MaterialSymbols.Rounded.Lists
            ),
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        ShortNavigationBar(arrangement = ShortNavigationBarArrangement.Centered) {
            items.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

                ShortNavigationBarItem(
                    iconPosition = NavigationItemIconPosition.Start,
                    icon = {
                        Image(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            colorFilter = ColorFilter.tint(
                                if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}