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
package com.rve.rvkernelmanager.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.io.File

data class AppSettings(val seedColorArgb: Int = 0xFFEBAC00.toInt())

object SettingsManager {
    private val configDir = File(System.getProperty("user.home"), ".config/rvkernel-manager")
    private val configFile = File(configDir, "rvkernel-manager.conf")

    fun loadSettings(): AppSettings = try {
        if (configFile.exists()) {
            val content = configFile.readText()

            val hexColorString = content.lines()
                .find { it.trim().startsWith("color_scheme") }
                ?.substringAfter("=")
                ?.trim()
                ?.removeSurrounding("\"")

            if (hexColorString != null) {
                try {
                    val cleanHex = hexColorString.removePrefix("#")
                    val colorInt = cleanHex.toLong(16).toInt()
                    AppSettings(seedColorArgb = colorInt)
                } catch (e: Exception) {
                    e.printStackTrace()
                    AppSettings()
                }
            } else {
                AppSettings()
            }
        } else {
            AppSettings()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        AppSettings()
    }

    fun saveColor(color: Color) {
        try {
            if (!configDir.exists()) {
                configDir.mkdirs()
            }

            val hexString = "#${"%08X".format(color.toArgb())}"

            val configContent = "color_scheme = \"$hexString\"\n"

            configFile.writeText(configContent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
