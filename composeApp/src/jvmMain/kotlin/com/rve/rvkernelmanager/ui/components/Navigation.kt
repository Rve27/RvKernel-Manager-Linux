@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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
import kotlinx.serialization.Serializable

object Navigation {
    @Serializable object Home
    @Serializable object CPU
    @Serializable object Kernel

    data class NavItem(
        val label: String,
        val route: Any,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector
    )

    @Composable
    fun BottomNavigationBar(
        navController: NavController,
        modifier: Modifier = Modifier
    ) {
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

        Surface(
            modifier = modifier,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .animateContentSize(
                        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    val isSelected =
                        currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true

                    val interactionSource = remember { MutableInteractionSource() }

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .clickable(
                                interactionSource = interactionSource,
                            ) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.animateContentSize(
                                animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                colorFilter = ColorFilter.tint(
                                    if (isSelected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                contentDescription = null
                            )
                            AnimatedVisibility(
                                visible = isSelected,
                            ) {
                                Text(
                                    text = item.label,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    maxLines = 1,
                                    softWrap = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}