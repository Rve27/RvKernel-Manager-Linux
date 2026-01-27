package com.rve.rvkernelmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import com.rve.rvkernelmanager.ui.components.AppBar.SimpleTopAppBar
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme

@Composable
fun RvKernelManagerApp() {
    RvKernelManagerTheme {
        HomeScreen(topBar = { SimpleTopAppBar() })
    }
}