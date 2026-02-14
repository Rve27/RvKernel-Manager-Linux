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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Bolt
import com.composables.icons.materialsymbols.roundedfilled.Check
import com.composables.icons.materialsymbols.roundedfilled.Close
import com.composables.icons.materialsymbols.roundedfilled.Manufacturing
import com.composables.icons.materialsymbols.roundedfilled.Memory
import com.composables.icons.materialsymbols.roundedfilled.Speed
import com.rve.rvkernelmanager.ui.viewmodel.CPUViewModel

@Composable
fun CPUScreen(viewModel: CPUViewModel = viewModel { CPUViewModel() }) {
    val cpuData by viewModel.cpuData.collectAsStateWithLifecycle()
    val availableFreqs by viewModel.availableFreqs.collectAsStateWithLifecycle()
    val availableGovernors by viewModel.availableGovernors.collectAsStateWithLifecycle()

    var showAvailableFreqsDialog by remember { mutableStateOf(false) }
    var showAvailableGovernorsDialog by remember { mutableStateOf(false) }
    var availableFreqsDialogTitle by remember { mutableStateOf("") }
    var isMaxFreq by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        viewModel.updateCurFreq()
        onDispose {
            viewModel.stopCpuJob()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp, start = 8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialShapes.Cookie9Sided.toShape())
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            imageVector = MaterialSymbols.RoundedFilled.Memory,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Column {
                        Text(
                            text = "CPU Manager",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "Monitor and control processor behavior",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 240.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                            shape = MaterialTheme.shapes.large,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column {
                                    Text(
                                        text = "Current Frequency",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                    )
                                    Text(
                                        text = "${cpuData.curFreq} MHz",
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                }
                                Icon(
                                    imageVector = MaterialSymbols.RoundedFilled.Speed,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }

                    item {
                        CpuStatCard(
                            title = "Minimum Frequency",
                            value = "${cpuData.minFreq} MHz",
                            icon = MaterialSymbols.RoundedFilled.Speed,
                            onClick = {
                                availableFreqsDialogTitle = "Select Minimum Frequency"
                                viewModel.getAvailableFreqs()
                                showAvailableFreqsDialog = true
                                isMaxFreq = false
                            },
                        )
                    }

                    item {
                        CpuStatCard(
                            title = "Maximum Frequency",
                            value = "${cpuData.maxFreq} MHz",
                            icon = MaterialSymbols.RoundedFilled.Speed,
                            onClick = {
                                availableFreqsDialogTitle = "Select Maximum Frequency"
                                viewModel.getAvailableFreqs()
                                showAvailableFreqsDialog = true
                                isMaxFreq = true
                            },
                        )
                    }

                    item {
                        CpuStatCard(
                            title = "Governor",
                            value = cpuData.governor,
                            icon = MaterialSymbols.RoundedFilled.Manufacturing,
                            onClick = {
                                viewModel.getAvailableGovernors()
                                showAvailableGovernorsDialog = true
                            },
                        )
                    }

                    if (cpuData.hasBoost) {
                        item {
                            CpuToggleCard(
                                title = "CPU Boost / Turbo",
                                checked = cpuData.isBoostEnabled,
                                onCheckedChange = { viewModel.toggleCpuBoost(it) },
                            )
                        }
                    }
                }
            }
        }

        if (showAvailableFreqsDialog) {
            AlertDialog(
                onDismissRequest = { showAvailableFreqsDialog = false },
                title = { Text(availableFreqsDialogTitle) },
                text = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy((4).dp),
                    ) {
                        if (availableFreqs.isEmpty()) {
                            item { Text("No frequencies found or access denied.") }
                        } else {
                            itemsIndexed(availableFreqs) { index, freq ->
                                val isChecked = if (isMaxFreq) freq == cpuData.maxFreq else freq == cpuData.minFreq
                                val shape = when (index) {
                                    0 ->
                                        (
                                            ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape
                                            )
                                            .copy(
                                                topStart = CornerSize(100),
                                                topEnd = CornerSize(100),
                                            )

                                    availableFreqs.lastIndex ->
                                        (
                                            ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape
                                            )
                                            .copy(
                                                bottomStart = CornerSize(100),
                                                bottomEnd = CornerSize(100),
                                            )

                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                }

                                ToggleButton(
                                    checked = isChecked,
                                    onCheckedChange = {
                                        viewModel.setCpuFrequency(freq, isMaxFreq)
                                        showAvailableFreqsDialog = false
                                    },
                                    contentPadding = PaddingValues(16.dp),
                                    shapes = ToggleButtonDefaults.shapes(
                                        shape = shape,
                                        checkedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                                    ),
                                    modifier = Modifier.fillMaxWidth().semantics { role = Role.RadioButton },
                                ) {
                                    Text("$freq MHz")
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAvailableFreqsDialog = false }) { Text("Close") }
                },
            )
        }

        if (showAvailableGovernorsDialog) {
            AlertDialog(
                onDismissRequest = { showAvailableGovernorsDialog = false },
                title = { Text("Select CPU Governor") },
                text = {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy((4).dp)) {
                        if (availableGovernors.isEmpty()) {
                            item { Text("No governors found or access denied.") }
                        } else {
                            itemsIndexed(availableGovernors) { index, governor ->
                                val shape = when (index) {
                                    0 ->
                                        (
                                            ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape
                                            )
                                            .copy(
                                                topStart = CornerSize(100),
                                                topEnd = CornerSize(100),
                                            )

                                    availableGovernors.lastIndex ->
                                        (
                                            ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape
                                            )
                                            .copy(
                                                bottomStart = CornerSize(100),
                                                bottomEnd = CornerSize(100),
                                            )

                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                }
                                ToggleButton(
                                    checked = cpuData.governor == governor,
                                    onCheckedChange = {
                                        viewModel.setCpuGovernor(governor)
                                        showAvailableGovernorsDialog = false
                                    },
                                    contentPadding = PaddingValues(16.dp),
                                    shapes = ToggleButtonDefaults.shapes(
                                        shape = shape,
                                        checkedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                                    ),
                                    modifier = Modifier.fillMaxWidth().semantics { role = Role.RadioButton },
                                ) {
                                    Text(governor)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAvailableGovernorsDialog = false }) { Text("Close") }
                },
            )
        }
    }
}

@Composable
fun CpuStatCard(title: String, value: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.height(140.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun CpuToggleCard(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    val animatedContainerColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "Container Color",
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "Text Color",
    )

    val animatedIconColor by animateColorAsState(
        targetValue = if (checked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "Icon Color",
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = animatedContainerColor,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        interactionSource = interactionSource,
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier.height(140.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                val slowSpatialSpec = MaterialTheme.motionScheme.slowSpatialSpec<Float>()
                val slowEffectsSpec = MaterialTheme.motionScheme.slowSpatialSpec<Float>()

                Icon(
                    imageVector = MaterialSymbols.RoundedFilled.Bolt,
                    contentDescription = null,
                    tint = animatedIconColor,
                )
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    interactionSource = interactionSource,
                    thumbContent = {
                        AnimatedContent(
                            targetState = checked,
                            transitionSpec = {
                                (
                                    scaleIn(
                                        animationSpec = slowSpatialSpec,
                                    ) + fadeIn(
                                        animationSpec = slowEffectsSpec,
                                    )
                                    ).togetherWith(
                                    scaleOut(
                                        animationSpec = slowSpatialSpec,
                                    ) + fadeOut(
                                        animationSpec = slowEffectsSpec,
                                    ),
                                )
                            },
                            label = "Switch thumb icon animation",
                        ) { isChecked ->
                            Icon(
                                imageVector = if (isChecked)
                                    MaterialSymbols.RoundedFilled.Check
                                else
                                    MaterialSymbols.RoundedFilled.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    },
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = animatedContentColor,
            )
        }
    }
}
