package com.rve.rvkernelmanager.ui.data

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.painter.Painter

data class HomeItem(
    val icon: Painter,
    val title: String,
    val summary: String,
)
