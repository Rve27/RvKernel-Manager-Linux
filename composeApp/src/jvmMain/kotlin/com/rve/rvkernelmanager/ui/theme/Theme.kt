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
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.rve.rvkernelmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.materialkolor.rememberDynamicColorScheme
import org.jetbrains.compose.resources.Font
import rvkernel_manager_linux.composeapp.generated.resources.Res
import rvkernel_manager_linux.composeapp.generated.resources.google_sans_flex

@Composable
fun RvKernelManagerTheme(seedColor: Color, isDark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = rememberDynamicColorScheme(seedColor = seedColor, isDark = isDark)

    val googleSansFontFamily = FontFamily(
        Font(Res.font.google_sans_flex, weight = FontWeight.Medium),
        Font(Res.font.google_sans_flex, weight = FontWeight.Normal),
        Font(Res.font.google_sans_flex, weight = FontWeight.Bold),
        Font(Res.font.google_sans_flex, weight = FontWeight.SemiBold),
    )

    val defaultTypography = Typography()
    val appTypography = Typography(
        displayLarge = defaultTypography.displayLarge.copy(fontFamily = googleSansFontFamily),
        displayMedium = defaultTypography.displayMedium.copy(fontFamily = googleSansFontFamily),
        displaySmall = defaultTypography.displaySmall.copy(fontFamily = googleSansFontFamily),
        headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = googleSansFontFamily),
        headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = googleSansFontFamily),
        headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = googleSansFontFamily),
        titleLarge = defaultTypography.titleLarge.copy(fontFamily = googleSansFontFamily),
        titleMedium = defaultTypography.titleMedium.copy(fontFamily = googleSansFontFamily),
        titleSmall = defaultTypography.titleSmall.copy(fontFamily = googleSansFontFamily),
        bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = googleSansFontFamily),
        bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = googleSansFontFamily),
        bodySmall = defaultTypography.bodySmall.copy(fontFamily = googleSansFontFamily),
        labelLarge = defaultTypography.labelLarge.copy(fontFamily = googleSansFontFamily),
        labelMedium = defaultTypography.labelMedium.copy(fontFamily = googleSansFontFamily),
        labelSmall = defaultTypography.labelSmall.copy(fontFamily = googleSansFontFamily),
    )

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = appTypography,
        content = content,
    )
}
