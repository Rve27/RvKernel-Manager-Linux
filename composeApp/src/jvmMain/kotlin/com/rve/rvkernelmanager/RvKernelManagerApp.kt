package com.rve.rvkernelmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rve.rvkernelmanager.ui.components.AppBar.SimpleTopAppBar
import com.rve.rvkernelmanager.ui.components.Navigation.BottomNavigationBar
import com.rve.rvkernelmanager.ui.screen.DummyScreen
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme
import com.rve.rvkernelmanager.ui.components.Navigation.Home
import com.rve.rvkernelmanager.ui.components.Navigation.CPU
import com.rve.rvkernelmanager.ui.components.Navigation.Kernel
import com.rve.rvkernelmanager.ui.screen.CPUScreen
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
                        DummyScreen()
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
        title = { Text("Select Theme Color") },
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
                    Slider(
                        value = red,
                        onValueChange = { red = it }
                    )
                }
                Column {
                    Text("Green: ${(green * 255).toInt()}")
                    Slider(
                        value = green,
                        onValueChange = { green = it }
                    )
                }
                Column {
                    Text("Blue: ${(blue * 255).toInt()}")
                    Slider(
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
