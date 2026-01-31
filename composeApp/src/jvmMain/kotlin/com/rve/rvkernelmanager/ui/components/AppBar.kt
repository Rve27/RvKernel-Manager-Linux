@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Dark_mode
import com.composables.icons.materialsymbols.roundedfilled.Light_mode

object AppBar {
    @Composable
    fun SimpleTopAppBar(
        isDarkTheme: Boolean,
        onThemeChange: () -> Unit
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "RvKernel Manager Linux",
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                IconButton(onClick = onThemeChange) {
                    val slideAnimationSpec = MaterialTheme.motionScheme.slowSpatialSpec<IntOffset>()
                    val fadeAnimationSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()

                    AnimatedContent(
                        targetState = isDarkTheme,
                        transitionSpec = {
                            if (targetState) {
                                (slideInVertically(slideAnimationSpec) { height -> -height } + fadeIn(fadeAnimationSpec)) togetherWith
                                        (slideOutVertically(slideAnimationSpec) { height -> height } + fadeOut(fadeAnimationSpec))
                            } else {
                                (slideInVertically(slideAnimationSpec) { height -> height } + fadeIn(fadeAnimationSpec)) togetherWith
                                        (slideOutVertically(slideAnimationSpec) { height -> -height } + fadeOut(fadeAnimationSpec))
                            }.using(
                                SizeTransform(clip = false)
                            )
                        },
                    ) { isDark ->
                        Image(
                            imageVector = if (isDark) MaterialSymbols.RoundedFilled.Light_mode else MaterialSymbols.RoundedFilled.Dark_mode,
                            contentDescription = if (isDark) "Switch to light mode" else "Switch to dark mode",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        )
    }
}