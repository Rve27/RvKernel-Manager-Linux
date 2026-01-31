@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Check
import com.composables.icons.materialsymbols.roundedfilled.Close
import com.rve.rvkernelmanager.ui.data.AppIcon

object List {
    @Composable
    fun ListItem(
        icon: AppIcon,
        containerIconShape: Shape = MaterialShapes.Circle.toShape(),
        title: String,
        summary: String,
        onClick: (() -> Unit)? = null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onClick?.invoke() })
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(containerIconShape)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (icon) {
                    is AppIcon.PainterIcon -> Image(
                        painter = icon.painter,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                    )
                    is AppIcon.ImageVectorIcon -> Image(
                        imageVector = icon.imageVector,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                    )
                }
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    @Composable
    fun SwitchListItem(
        icon: AppIcon,
        containerIconShape: Shape = MaterialShapes.Circle.toShape(),
        text: String,
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onCheckedChange(!checked) })
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(containerIconShape)
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (icon) {
                    is AppIcon.PainterIcon -> Image(
                        painter = icon.painter,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                    )
                    is AppIcon.ImageVectorIcon -> Image(
                        imageVector = icon.imageVector,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiary)
                    )
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                thumbContent = {
                    Crossfade(
                        targetState = checked,
                        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec()
                    ) { isChecked ->
                        Image(
                            imageVector = if (isChecked) MaterialSymbols.RoundedFilled.Check else MaterialSymbols.RoundedFilled.Close,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHighest),
                            modifier = Modifier.size(SwitchDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}