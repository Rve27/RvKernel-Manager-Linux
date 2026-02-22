/*
 * Copyright (c) 2026 Rve <rve27github@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

// Dear programmer:
// When I wrote this code, only god and
// I knew how it worked.
// Now, only god knows it!
//
// Therefore, if you are trying to optimize
// this routine and it fails (most surely),
// please increase this counter as a
// warning for the next person:
//
// total hours wasted here = 254
//
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Computer
import com.composables.icons.materialsymbols.roundedfilled.Memory
import com.composables.icons.materialsymbols.roundedfilled.Person
import com.composables.icons.materialsymbols.roundedfilled.View_in_ar
import com.rve.rvkernelmanager.ui.components.Card.ItemCard
import com.rve.rvkernelmanager.ui.data.AppIcon
import com.rve.rvkernelmanager.ui.data.HomeItem
import com.rve.rvkernelmanager.ui.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource
import rvkernel_manager_linux.composeapp.generated.resources.Res
import rvkernel_manager_linux.composeapp.generated.resources.arch_linux_logo
import rvkernel_manager_linux.composeapp.generated.resources.cachyos_logo
import rvkernel_manager_linux.composeapp.generated.resources.fedora_logo
import rvkernel_manager_linux.composeapp.generated.resources.ic_linux
import rvkernel_manager_linux.composeapp.generated.resources.linux_mint_logo
import rvkernel_manager_linux.composeapp.generated.resources.manjaro_logo
import rvkernel_manager_linux.composeapp.generated.resources.ubuntu_logo

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel { HomeViewModel() }) {
    val deviceInfo by viewModel.deviceInfo.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.updateRamStatus()
        onDispose {
            viewModel.stopRamJob()
        }
    }

    Scaffold { innerPadding ->
        val osIcons = when {
            deviceInfo.os.contains("CachyOS") -> AppIcon.PainterIcon(painterResource(Res.drawable.cachyos_logo))
            deviceInfo.os.contains("Ubuntu") -> AppIcon.PainterIcon(painterResource(Res.drawable.ubuntu_logo))
            deviceInfo.os.contains("Linux Mint") -> AppIcon.PainterIcon(painterResource(Res.drawable.linux_mint_logo))
            deviceInfo.os.contains("Arch Linux") -> AppIcon.PainterIcon(painterResource(Res.drawable.arch_linux_logo))
            deviceInfo.os.contains("Manjaro") -> AppIcon.PainterIcon(painterResource(Res.drawable.manjaro_logo))
            deviceInfo.os.contains("Fedora") -> AppIcon.PainterIcon(painterResource(Res.drawable.fedora_logo))
            else -> AppIcon.PainterIcon(painterResource(Res.drawable.ic_linux))
        }

        val systemInfoItems = listOf(
            HomeItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Person),
                containerIconShape = MaterialShapes.Ghostish.toShape(),
                title = "User",
                summary = deviceInfo.user,
            ),
            HomeItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.Rounded.Computer),
                containerIconShape = MaterialShapes.Clover4Leaf.toShape(),
                title = "Hostname",
                summary = deviceInfo.hostname,
            ),
            HomeItem(
                icon = osIcons,
                containerIconShape = MaterialShapes.Cookie7Sided.toShape(),
                title = "OS",
                summary = deviceInfo.os,
            ),
            HomeItem(
                icon = AppIcon.PainterIcon(painterResource(Res.drawable.ic_linux)),
                containerIconShape = MaterialShapes.Circle.toShape(),
                title = "Kernel",
                summary = deviceInfo.kernel,
            ),
        )

        val hardwareInfoItems = listOf(
            HomeItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Memory),
                containerIconShape = MaterialShapes.Square.toShape(),
                title = "CPU",
                summary = deviceInfo.cpu,
            ),
            HomeItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.View_in_ar),
                containerIconShape = MaterialShapes.Gem.toShape(),
                title = "GPU",
                summary = deviceInfo.gpu,
            ),
        )

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 340.dp),
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "System Information",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp),
                    )
                }

                items(systemInfoItems) { item ->
                    ItemCard(
                        icon = item.icon,
                        containerIconShape = item.containerIconShape,
                        title = item.title,
                        summary = item.summary,
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = "Hardware Resources",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 8.dp),
                    )
                }

                items(hardwareInfoItems) { item ->
                    ItemCard(
                        icon = item.icon,
                        containerIconShape = item.containerIconShape,
                        title = item.title,
                        summary = item.summary,
                    )
                }

                item(span = { GridItemSpan(minOf(2, maxLineSpan)) }) {
                    RamUsageCard(
                        ramTotal = deviceInfo.ramTotal,
                        ramUsed = deviceInfo.ramUsed,
                        ramFree = deviceInfo.ramFree,
                    )
                }

                if (deviceInfo.isZramActive || deviceInfo.isSwapActive) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "Virtual Memory",
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 8.dp),
                        )
                    }
                }

                if (deviceInfo.isZramActive) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(
                                animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
                            ) + expandIn(
                                animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
                            ),
                            exit = shrinkOut(
                                animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
                            ) + fadeOut(
                                animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
                            ),
                        ) {
                            ItemCard(
                                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Memory),
                                containerIconShape = MaterialShapes.Square.toShape(),
                                title = "ZRAM",
                                summary = deviceInfo.zram,
                            )
                        }
                    }
                }

                if (deviceInfo.isSwapActive) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(
                                animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
                            ) + expandIn(
                                animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
                            ),
                            exit = shrinkOut(
                                animationSpec = MaterialTheme.motionScheme.slowSpatialSpec(),
                            ) + fadeOut(
                                animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
                            ),
                        ) {
                            ItemCard(
                                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Memory),
                                containerIconShape = MaterialShapes.Square.toShape(),
                                title = "Total Swap",
                                summary = deviceInfo.swap,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RamUsageCard(ramTotal: String, ramUsed: String, ramFree: String) {
    val totalVal = ramTotal.substringBefore(" ").toFloatOrNull() ?: 0f
    val usedVal = ramUsed.substringBefore(" ").toFloatOrNull() ?: 0f
    val progress = if (totalVal > 0f) usedVal / totalVal else 0f

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(MaterialShapes.Square.toShape())
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = MaterialSymbols.RoundedFilled.Memory,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Column {
                    Text(
                        text = "RAM Usage",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "$ramUsed / $ramTotal (Free: $ramFree)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            LinearWavyProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            )
        }
    }
}
