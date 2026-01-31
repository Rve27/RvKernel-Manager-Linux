package com.rve.rvkernelmanager.ui.data

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Shape

data class HomeItem(
    val icon: AppIcon,
    val containerIconShape: Shape = CircleShape,
    val title: String,
    val summary: String,
)
