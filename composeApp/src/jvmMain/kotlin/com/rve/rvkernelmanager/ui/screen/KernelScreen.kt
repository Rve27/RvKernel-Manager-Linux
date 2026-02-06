@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Developer_board
import com.composables.icons.materialsymbols.roundedfilled.Speed
import com.rve.rvkernelmanager.ui.components.List.ListItem
import com.rve.rvkernelmanager.ui.data.AppIcon
import com.rve.rvkernelmanager.ui.data.kernel.KernelItem
import com.rve.rvkernelmanager.ui.viewmodel.KernelViewModel

@Composable
fun KernelScreen() {
    val viewModel = KernelViewModel()
    val uclampData by viewModel.uclampData.collectAsStateWithLifecycle()

    var showUclampDialog by remember { mutableStateOf(false) }
    var uclampDialogTitle by remember { mutableStateOf("") }
    var uclampTarget by remember { mutableStateOf("") }
    var uclampValue by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        val kernelItems = listOf(
            KernelItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Max",
                summary = uclampData.max.toString(),
                onClick = {
                    uclampDialogTitle = "UClamp max"
                    uclampTarget = "max"
                    uclampValue = uclampData.max.toString()
                    showUclampDialog = true
                }
            ),
            KernelItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Min",
                summary = uclampData.min.toString(),
                onClick = {
                    uclampDialogTitle = "UClamp min"
                    uclampTarget = "min"
                    uclampValue = uclampData.min.toString()
                    showUclampDialog = true
                }
            ),
            KernelItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Min RT default",
                summary = uclampData.minRt.toString(),
                onClick = {
                    uclampDialogTitle = "UClamp min RT default"
                    uclampTarget = "min_rt_default"
                    uclampValue = uclampData.max.toString()
                    showUclampDialog = true
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
                    if (uclampData.hasUclamp) {
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
                                            .clip(MaterialShapes.Square.toShape())
                                            .background(MaterialTheme.colorScheme.primary)
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            imageVector = MaterialSymbols.RoundedFilled.Developer_board,
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                                            contentDescription = null
                                        )
                                    }
                                    Text(
                                        text = "UClamp Parameters",
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
                        items(kernelItems) { item ->
                            ListItem(
                                icon = item.icon,
                                title = item.title,
                                summary = item.summary,
                                onClick = item.onClick
                            )
                        }
                    }
                }
            }
        }

        if (showUclampDialog) {
            val onApply = {
                val value = uclampValue.toIntOrNull()
                if (value != null) {
                    viewModel.setUclampValue(uclampTarget, value)
                }
                showUclampDialog = false
            }

            AlertDialog(
                onDismissRequest = { showUclampDialog = false },
                title = { Text("Set value for $uclampDialogTitle") },
                text = {
                    OutlinedTextField(
                        value = uclampValue,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                uclampValue = newValue
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { onApply() }
                        ),
                        label = { Text("Value") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { onApply() },
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text("Apply")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUclampDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}