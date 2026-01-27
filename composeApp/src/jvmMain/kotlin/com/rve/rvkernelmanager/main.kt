package com.rve.rvkernelmanager

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "RvKernel-Manager-Desktop",
    ) {
        App()
    }
}