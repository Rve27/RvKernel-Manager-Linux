package com.rve.rvkernelmanager.ui.data

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface AppIcon {
    data class PainterIcon(val painter: Painter) : AppIcon
    data class ImageVectorIcon(val imageVector: ImageVector) : AppIcon
}
