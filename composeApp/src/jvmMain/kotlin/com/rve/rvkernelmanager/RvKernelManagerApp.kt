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
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Add
import com.composables.icons.materialsymbols.roundedfilled.Airline_seat_flat
import com.composables.icons.materialsymbols.roundedfilled.Close
import com.composables.icons.materialsymbols.roundedfilled.Power_settings_new
import com.composables.icons.materialsymbols.roundedfilled.Remove
import com.composables.icons.materialsymbols.roundedfilled.Restart_alt
import com.composables.icons.materialsymbols.roundedfilled.Settings
import com.rve.rvkernelmanager.ui.components.AppBar.SimpleTopAppBar
import com.rve.rvkernelmanager.ui.components.Navigation.BottomNavigationBar
import com.rve.rvkernelmanager.ui.components.Navigation.CPU
import com.rve.rvkernelmanager.ui.components.Navigation.Home
import com.rve.rvkernelmanager.ui.components.Navigation.Kernel
import com.rve.rvkernelmanager.ui.screen.CPUScreen
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.screen.KernelScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme
import com.rve.rvkernelmanager.utils.PowerUtils.reboot
import com.rve.rvkernelmanager.utils.PowerUtils.rebootToFirmware
import com.rve.rvkernelmanager.utils.PowerUtils.shutdown
import com.rve.rvkernelmanager.utils.PowerUtils.sleep
import com.rve.rvkernelmanager.utils.SettingsManager

@Composable
fun RvKernelManagerApp() {
    val navController = rememberNavController()

    val loadSettings = remember { SettingsManager.loadSettings() }

    var seedColor by remember { mutableStateOf(Color(loadSettings.seedColorArgb)) }
    var showColorPicker by remember { mutableStateOf(false) }

    val systemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }

    val focusRequester = remember { FocusRequester() }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val fabItems = listOf(
        MaterialSymbols.RoundedFilled.Power_settings_new to "Shutdown",
        MaterialSymbols.RoundedFilled.Restart_alt to "Reboot",
        MaterialSymbols.RoundedFilled.Settings to "Reboot to firmware settings",
        MaterialSymbols.RoundedFilled.Airline_seat_flat to "Sleep",
    )

    RvKernelManagerTheme(
        seedColor = seedColor,
        isDark = isDarkTheme,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    SimpleTopAppBar(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = !isDarkTheme },
                        openColorPicker = { showColorPicker = true },
                    )
                },
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    modifier = Modifier.padding(innerPadding),
                ) {
                    composable<Home> {
                        HomeScreen()
                    }
                    composable<CPU> {
                        CPUScreen()
                    }
                    composable<Kernel> {
                        KernelScreen()
                    }
                }
            }

            BottomNavigationBar(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
            )

            FloatingActionButtonMenu(
                modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
                expanded = fabMenuExpanded,
                button = {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                            if (fabMenuExpanded) TooltipAnchorPosition.Start else TooltipAnchorPosition.Above,
                        ),
                        tooltip = { PlainTooltip { Text("Power Menu") } },
                        state = rememberTooltipState(),
                    ) {
                        ToggleFloatingActionButton(
                            modifier = Modifier
                                .semantics {
                                    traversalIndex = -1f
                                    stateDescription = if (fabMenuExpanded) "Expanded" else "Collapsed"
                                    contentDescription = "Toggle power menu"
                                }
                                .animateFloatingActionButton(
                                    visible = true,
                                    alignment = Alignment.BottomEnd,
                                )
                                .focusRequester(focusRequester),
                            checked = fabMenuExpanded,
                            onCheckedChange = { fabMenuExpanded = !fabMenuExpanded },
                        ) {
                            val imageVector by remember {
                                derivedStateOf {
                                    if (checkedProgress >
                                        0.5f
                                    ) MaterialSymbols.RoundedFilled.Close else MaterialSymbols.RoundedFilled.Power_settings_new
                                }
                            }
                            Icon(
                                painter = rememberVectorPainter(imageVector),
                                contentDescription = null,
                                modifier = Modifier.animateIcon({ checkedProgress }),
                            )
                        }
                    }
                },
            ) {
                fabItems.forEachIndexed { i, item ->
                    FloatingActionButtonMenuItem(
                        modifier = Modifier
                            .semantics {
                                isTraversalGroup = true
                                if (i == fabItems.size - 1) {
                                    customActions = listOf(
                                        CustomAccessibilityAction(
                                            label = "Close menu",
                                            action = {
                                                fabMenuExpanded = false
                                                true
                                            },
                                        ),
                                    )
                                }
                            }
                            .then(
                                if (i == 0) {
                                    Modifier.onKeyEvent {
                                        if (it.type == KeyEventType.KeyDown &&
                                            (it.key == Key.DirectionUp || (it.isShiftPressed && it.key == Key.Tab))
                                        ) {
                                            focusRequester.requestFocus()
                                            return@onKeyEvent true
                                        }
                                        return@onKeyEvent false
                                    }
                                } else {
                                    Modifier
                                },
                            ),
                        onClick = {
                            fabMenuExpanded = false

                            when (item.second) {
                                "Shutdown" -> shutdown()
                                "Reboot" -> reboot()
                                "Reboot to firmware settings" -> rebootToFirmware()
                                "Sleep" -> sleep()
                            }
                        },
                        icon = { Icon(rememberVectorPainter(item.first), contentDescription = null) },
                        text = { Text(text = item.second) },
                    )
                }
            }

            if (showColorPicker) {
                ColorPickerDialog(
                    color = seedColor,
                    onDismiss = { showColorPicker = false },
                    onColorSelected = { newColor ->
                        seedColor = newColor
                        SettingsManager.saveColor(newColor)
                        showColorPicker = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ColorPickerDialog(color: Color, onDismiss: () -> Unit, onColorSelected: (Color) -> Unit) {
    val defaultColor = Color(0xFFEBAC00)

    var red by remember { mutableStateOf(color.red) }
    var green by remember { mutableStateOf(color.green) }
    var blue by remember { mutableStateOf(color.blue) }

    var hexString by remember {
        mutableStateOf(
            String.format("%06X", (color.toArgb() and 0xFFFFFF)),
        )
    }

    val currentColor = Color(red, green, blue)

    LaunchedEffect(red, green, blue) {
        hexString = String.format("%06X", (currentColor.toArgb() and 0xFFFFFF))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Select Theme Color",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = {
                        red = defaultColor.red
                        green = defaultColor.green
                        blue = defaultColor.blue
                    },
                ) {
                    Image(
                        imageVector = MaterialSymbols.RoundedFilled.Restart_alt,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(currentColor),
                )
                Column {
                    Text("Red: ${(red * 255).toInt()}")
                    SliderWithTrackIcons(
                        value = red,
                        onValueChange = { red = it },
                    )
                }
                Column {
                    Text("Green: ${(green * 255).toInt()}")
                    SliderWithTrackIcons(
                        value = green,
                        onValueChange = { green = it },
                    )
                }
                Column {
                    Text("Blue: ${(blue * 255).toInt()}")
                    SliderWithTrackIcons(
                        value = blue,
                        onValueChange = { blue = it },
                    )
                }

                OutlinedTextField(
                    value = hexString,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() || it in 'A'..'F' || it in 'a'..'f' }
                            .take(6)
                            .uppercase()
                        hexString = filtered

                        if (filtered.length == 6) {
                            try {
                                val colorInt = filtered.toLong(16)
                                val newColor = Color(colorInt + 0xFF000000)
                                red = newColor.red
                                green = newColor.green
                                blue = newColor.blue
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    },
                    label = { Text("Hex Code") },
                    prefix = { Text("#") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onColorSelected(currentColor) },
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onColorSelected(currentColor) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

@Composable
private fun SliderWithTrackIcons(value: Float, onValueChange: (Float) -> Unit) {
    val startIcon = rememberVectorPainter(MaterialSymbols.RoundedFilled.Remove)
    val endIcon = rememberVectorPainter(MaterialSymbols.RoundedFilled.Add)

    Slider(
        value = value,
        onValueChange = onValueChange,
        track = { sliderState ->
            val iconSize = DpSize(20.dp, 20.dp)
            val iconPadding = 10.dp
            val thumbTrackGapSize = 6.dp
            val activeIconColor = SliderDefaults.colors().activeTickColor
            val inactiveIconColor = SliderDefaults.colors().inactiveTickColor

            val trackIconStart: DrawScope.(Offset, Color) -> Unit = { offset, color ->
                translate(offset.x + iconPadding.toPx(), offset.y) {
                    with(startIcon) {
                        draw(iconSize.toSize(), colorFilter = ColorFilter.tint(color))
                    }
                }
            }
            val trackIconEnd: DrawScope.(Offset, Color) -> Unit = { offset, color ->
                translate(offset.x - iconPadding.toPx() - iconSize.toSize().width, offset.y) {
                    with(endIcon) {
                        draw(iconSize.toSize(), colorFilter = ColorFilter.tint(color))
                    }
                }
            }

            SliderDefaults.Track(
                sliderState = sliderState,
                modifier = Modifier
                    .height(36.dp)
                    .drawWithContent {
                        drawContent()

                        val yOffset = size.height / 2 - iconSize.toSize().height / 2
                        val activeTrackStart = 0f
                        val activeTrackEnd = size.width * sliderState.coercedValueAsFraction - thumbTrackGapSize.toPx()

                        val inactiveTrackStart = activeTrackEnd + thumbTrackGapSize.toPx() * 2
                        val inactiveTrackEnd = size.width

                        val activeTrackWidth = activeTrackEnd - activeTrackStart
                        val inactiveTrackWidth = inactiveTrackEnd - inactiveTrackStart

                        if (iconSize.toSize().width < activeTrackWidth - iconPadding.toPx() * 2) {
                            trackIconStart(Offset(activeTrackStart, yOffset), activeIconColor)
                            trackIconEnd(Offset(activeTrackEnd, yOffset), activeIconColor)
                        }
                        if (iconSize.toSize().width < inactiveTrackWidth - iconPadding.toPx() * 2) {
                            trackIconStart(Offset(inactiveTrackStart, yOffset), inactiveIconColor)
                            trackIconEnd(Offset(inactiveTrackEnd, yOffset), inactiveIconColor)
                        }
                    },
                trackCornerSize = 12.dp,
                drawStopIndicator = null,
                thumbTrackGapSize = thumbTrackGapSize,
            )
        },
    )
}
