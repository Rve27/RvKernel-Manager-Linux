@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Bolt
import com.composables.icons.materialsymbols.roundedfilled.Manufacturing
import com.composables.icons.materialsymbols.roundedfilled.Memory
import com.composables.icons.materialsymbols.roundedfilled.Speed
import com.rve.rvkernelmanager.ui.components.List.ListItem
import com.rve.rvkernelmanager.ui.components.List.SwitchListItem
import com.rve.rvkernelmanager.ui.data.AppIcon
import com.rve.rvkernelmanager.ui.data.cpu.CPUItem
import com.rve.rvkernelmanager.ui.viewmodel.CPUViewModel

@Composable
fun CPUScreen() {
    val viewModel = CPUViewModel()
    val cpuInfo by viewModel.cpuInfo.collectAsStateWithLifecycle()
    val availableFreqs by viewModel.availableFreqs.collectAsStateWithLifecycle()
    val availableGovernors by viewModel.availableGovernors.collectAsStateWithLifecycle()

    var showAvailableFreqsDialog by remember { mutableStateOf(false) }
    var showAvailableGovernorsDialog by remember { mutableStateOf(false) }
    var availableFreqsDialogTitle by remember { mutableStateOf("") }
    var isMaxFreq by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        val cpuItems = listOf(
            CPUItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Minimum frequency",
                summary = "${cpuInfo.minFreq} MHz",
                onClick = {
                    availableFreqsDialogTitle = "Select Minimum Frequency"
                    viewModel.getAvailableFreqs()
                    showAvailableFreqsDialog = true
                    isMaxFreq = false
                }
            ),
            CPUItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Maximum frequency",
                summary = "${cpuInfo.maxFreq} MHz",
                onClick = {
                    availableFreqsDialogTitle = "Select Maximum Frequency"
                    viewModel.getAvailableFreqs()
                    showAvailableFreqsDialog = true
                    isMaxFreq = true
                }
            ),
            CPUItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Manufacturing),
                title = "Governor",
                summary = cpuInfo.governor,
                onClick = {
                    viewModel.getAvailableGovernors()
                    showAvailableGovernorsDialog = true
                }
            ),
        )

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Card(
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                LazyColumn {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = MaterialSymbols.RoundedFilled.Memory,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                                        contentDescription = null
                                    )
                                }
                                Text(
                                    text = "CPU",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    items(cpuItems) { item ->
                        ListItem(
                            icon = item.icon,
                            title = item.title,
                            summary = item.summary,
                            onClick = item.onClick
                        )
                    }
                    if (cpuInfo.hasBoost) {
                        item {
                            SwitchListItem(
                                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Bolt),
                                text = "CPU Boost / Turbo",
                                checked = cpuInfo.isBoostEnabled,
                                onCheckedChange = { viewModel.toggleCpuBoost(it) }
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
                        verticalArrangement = Arrangement.spacedBy((4).dp)
                    ) {
                        if (availableFreqs.isEmpty()) {
                            item {
                                Text("No frequencies found or access denied.")
                            }
                        } else {
                            itemsIndexed(availableFreqs) { index, freq ->
                                val isChecked = if (isMaxFreq) {
                                    freq == cpuInfo.maxFreq
                                } else {
                                    freq == cpuInfo.minFreq
                                }

                                val shape = when (index) {
                                    0 ->
                                        (ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape)
                                            .copy(
                                                topStart = CornerSize(100),
                                                topEnd = CornerSize(100)
                                            )

                                    availableFreqs.lastIndex ->
                                        (ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape)
                                            .copy(
                                                bottomStart = CornerSize(100),
                                                bottomEnd = CornerSize(100)
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
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .semantics { role = Role.RadioButton },
                                ) {
                                    Text("$freq MHz")
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showAvailableFreqsDialog = false },
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text("Close")
                    }
                }
            )
        }

        if (showAvailableGovernorsDialog) {
            AlertDialog(
                onDismissRequest = { showAvailableGovernorsDialog = false },
                title = { Text("Select CPU Governor") },
                text = {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy((4).dp)
                    ) {
                        if (availableGovernors.isEmpty()) {
                            item {
                                Text("No governors found or access denied.")
                            }
                        } else {
                            itemsIndexed(availableGovernors) { index, governor ->
                                val shape = when (index) {
                                    0 ->
                                        (ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape)
                                            .copy(
                                                topStart = CornerSize(100),
                                                topEnd = CornerSize(100)
                                            )

                                    availableGovernors.lastIndex ->
                                        (ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                                as RoundedCornerShape)
                                            .copy(
                                                bottomStart = CornerSize(100),
                                                bottomEnd = CornerSize(100)
                                            )

                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes().shape
                                }
                                ToggleButton(
                                    checked = cpuInfo.governor == governor,
                                    onCheckedChange = {
                                        viewModel.setCpuGovernor(governor)
                                        showAvailableGovernorsDialog = false
                                    },
                                    contentPadding = PaddingValues(16.dp),
                                    shapes = ToggleButtonDefaults.shapes(
                                        shape = shape,
                                        checkedShape = ButtonGroupDefaults.connectedButtonCheckedShape,
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .semantics { role = Role.RadioButton },
                                ) {
                                    Text(governor)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showAvailableGovernorsDialog = false },
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text("Close")
                    }
                }
            )
        }
    }
}