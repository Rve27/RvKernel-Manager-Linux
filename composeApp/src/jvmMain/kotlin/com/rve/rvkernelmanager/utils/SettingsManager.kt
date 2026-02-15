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

data class AppSettings(
    val seedColorArgb: Int = 0xFFEBAC00.toInt(),
    val applyTurboOnBoot: Boolean = false
)

object SettingsManager {
    private val configDir = File(System.getProperty("user.home"), ".config/rvkernel-manager")
    private val configFile = File(configDir, "rvkernel-manager.conf")

    fun loadSettings(): AppSettings = try {
        if (configFile.exists()) {
            val content = configFile.readText()
            val lines = content.lines()

            fun getValue(key: String): String? = lines
                .find { it.trim().startsWith(key) }
                ?.substringAfter("=")
                ?.trim()
                ?.removeSurrounding("\"")
                ?.removeSurrounding("'")

            val hexColorString = getValue("colorScheme")
            val applyTurbo = getValue("applyTurboOnBoot")?.toBoolean() ?: false

            val colorInt = if (!hexColorString.isNullOrEmpty()) {
                try {
                    hexColorString.removePrefix("#").toLong(16).toInt()
                } catch (e: Exception) { 0xFFEBAC00.toInt() }
            } else {
                0xFFEBAC00.toInt()
            }

            AppSettings(colorInt, applyTurbo)
        } else {
            AppSettings()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        AppSettings()
    }

    private fun saveConfig(settings: AppSettings) {
        try {
            if (!configDir.exists()) configDir.mkdirs()
            val hexString = "#${"%08X".format(settings.seedColorArgb)}"

            val content = buildString {
                appendLine("colorScheme = \"$hexString\"")
                appendLine("applyTurboOnBoot = ${settings.applyTurboOnBoot}")
            }
            configFile.writeText(content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveColor(color: Color) {
        val current = loadSettings()
        saveConfig(current.copy(seedColorArgb = color.toArgb()))
    }

    fun saveTurboBootSetting(applyOnBoot: Boolean) {
        val current = loadSettings()
        saveConfig(current.copy(applyTurboOnBoot = applyOnBoot))
    }
}