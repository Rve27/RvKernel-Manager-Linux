package com.rve.rvkernelmanager

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme
import com.rve.rvkernelmanager.ui.viewmodel.HomeViewModel

@Composable
@Preview
fun App() {
    RvKernelManagerTheme {
        HomeScreen(viewModel = HomeViewModel())
    }
}