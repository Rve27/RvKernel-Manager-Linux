package com.rve.rvkernelmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme
import com.rve.rvkernelmanager.ui.components.AppBar.SimpleTopAppBar

@Composable
@Preview
fun RvKernelManagerApp() {
    RvKernelManagerTheme {
        HomeScreen(topBar = { SimpleTopAppBar() })
    }
}