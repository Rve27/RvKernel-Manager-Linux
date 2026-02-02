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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Add
import com.composables.icons.materialsymbols.roundedfilled.Remove
import com.composables.icons.materialsymbols.roundedfilled.Restart_alt
import com.rve.rvkernelmanager.ui.components.AppBar.SimpleTopAppBar
import com.rve.rvkernelmanager.ui.components.Navigation.BottomNavigationBar
import com.rve.rvkernelmanager.ui.components.Navigation.CPU
import com.rve.rvkernelmanager.ui.components.Navigation.Home
import com.rve.rvkernelmanager.ui.components.Navigation.Kernel
import com.rve.rvkernelmanager.ui.screen.CPUScreen
import com.rve.rvkernelmanager.ui.screen.DummyScreen
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.screen.KernelScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme
import com.rve.rvkernelmanager.util.SettingsManager

@Composable
fun RvKernelManagerApp() {
    val navController = rememberNavController()

    val loadSettings = remember { SettingsManager.loadSettings() }

    var seedColor by remember { mutableStateOf(Color(loadSettings.seedColorArgb)) }
    var showColorPicker by remember { mutableStateOf(false) }

    val systemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemInDarkTheme) }

    RvKernelManagerTheme(
        seedColor = seedColor,
        isDark = isDarkTheme
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    SimpleTopAppBar(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { isDarkTheme = !isDarkTheme },
                        openColorPicker = { showColorPicker = true }
                    )
                },
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    modifier = Modifier.padding(innerPadding)
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
                    .padding(16.dp)
            )

            if (showColorPicker) {
                ColorPickerDialog(
                    color = seedColor,
                    onDismiss = { showColorPicker = false },
                    onColorSelected = { newColor ->
                        seedColor = newColor
                        SettingsManager.saveColor(newColor)
                        showColorPicker = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ColorPickerDialog(
    color: Color,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val defaultColor = Color(0xFFEBAC00)

    var red by remember { mutableStateOf(color.red) }
    var green by remember { mutableStateOf(color.green) }
    var blue by remember { mutableStateOf(color.blue) }

    var hexString by remember {
        mutableStateOf(
            String.format("%06X", (color.toArgb() and 0xFFFFFF))
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Theme Color",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
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
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(currentColor)
                )
                Column {
                    Text("Red: ${(red * 255).toInt()}")
                    SliderWithTrackIcons(
                        value = red,
                        onValueChange = { red = it }
                    )
                }
                Column {
                    Text("Green: ${(green * 255).toInt()}")
                    SliderWithTrackIcons(
                        value = green,
                        onValueChange = { green = it }
                    )
                }
                Column {
                    Text("Blue: ${(blue * 255).toInt()}")
                    SliderWithTrackIcons(
                        value = blue,
                        onValueChange = { blue = it }
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
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
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
        }
    )
}

@Composable
private fun SliderWithTrackIcons(
    value: Float,
    onValueChange: (Float) -> Unit,
) {
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
        }
    )
}
