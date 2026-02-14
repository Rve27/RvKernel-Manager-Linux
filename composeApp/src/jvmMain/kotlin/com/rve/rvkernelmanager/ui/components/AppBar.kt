/*
 * Copyright (c) 2026 Rve <rve27github@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

// Dear programmer:
// When I wrote this code, only god and
// I knew how it worked.
// Now, only god knows it!
//
// Therefore, if you are trying to optimize
// this routine and it fails (most surely),
// please increase this counter as a
// warning for the next person:
//
// total hours wasted here = 254
//
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
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Dark_mode
import com.composables.icons.materialsymbols.roundedfilled.Light_mode
import com.composables.icons.materialsymbols.roundedfilled.Palette

object AppBar {
    @Composable
    fun SimpleTopAppBar(isDarkTheme: Boolean, onThemeChange: () -> Unit, openColorPicker: () -> Unit) {
        TopAppBar(
            title = {
                Text(
                    text = "RvKernel Manager Linux",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            actions = {
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Left),
                    tooltip = {
                        PlainTooltip(caretShape = TooltipDefaults.caretShape()) { Text("Change theme color") }
                    },
                    state = rememberTooltipState(),
                ) {
                    IconButton(onClick = openColorPicker) {
                        Image(
                            imageVector = MaterialSymbols.RoundedFilled.Palette,
                            contentDescription = "Change Color Scheme",
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        )
                    }
                }
                TooltipBox(
                    positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Left),
                    tooltip = {
                        PlainTooltip(
                            caretShape = TooltipDefaults.caretShape()
                        ) {
                            Text(if (isDarkTheme) "Switch to light mode" else "Switch to dark mode")
                        }
                    },
                    state = rememberTooltipState(),
                ) {
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
                                    SizeTransform(clip = false),
                                )
                            },
                        ) { isDark ->
                            Image(
                                imageVector = if (isDark) MaterialSymbols.RoundedFilled.Light_mode else MaterialSymbols.RoundedFilled.Dark_mode,
                                contentDescription = if (isDark) "Switch to light mode" else "Switch to dark mode",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            )
                        }
                    }
                }
            },
        )
    }
}
