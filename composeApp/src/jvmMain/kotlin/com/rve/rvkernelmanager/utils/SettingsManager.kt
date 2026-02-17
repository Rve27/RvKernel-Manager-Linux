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
    val isDark: Boolean? = null,
)

object SettingsManager {
    private val configDir = File(System.getProperty("user.home"), ".config/rvkernel-manager")
    private val configFile = File(configDir, "rvkernel-manager.conf")

    private fun readConfigMap(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        if (configFile.exists()) {
            configFile.readLines().forEach { line ->
                val trimmed = line.trim()
                if (trimmed.contains("=")) {
                    val key = trimmed.substringBefore("=").trim()
                    val value = trimmed.substringAfter("=").trim().removeSurrounding("\"")
                    map[key] = value
                }
            }
        }
        return map
    }

    private fun writeConfigMap(map: Map<String, String>) {
        if (!configDir.exists()) {
            configDir.mkdirs()
        }
        val content = map.entries.joinToString("\n") { (key, value) ->
            if (value == "true" || value == "false") {
                "$key = $value"
            } else {
                "$key = \"$value\""
            }
        }
        configFile.writeText(content + "\n")
    }

    fun loadSettings(): AppSettings = try {
        val map = readConfigMap()

        val seedColorArgb = map["color_scheme"]?.let { hexString ->
            try {
                val cleanHex = hexString.removePrefix("#")
                cleanHex.toLong(16).toInt()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: AppSettings().seedColorArgb

        val isDark = map["dark_mode"]?.toBooleanStrictOrNull()

        AppSettings(seedColorArgb = seedColorArgb, isDark = isDark)
    } catch (e: Exception) {
        e.printStackTrace()
        AppSettings()
    }

    fun saveColor(color: Color) {
        try {
            val map = readConfigMap()
            map["color_scheme"] = "#${"%08X".format(color.toArgb())}"
            writeConfigMap(map)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveDarkMode(isDark: Boolean) {
        try {
            val map = readConfigMap()
            map["dark_mode"] = isDark.toString()
            writeConfigMap(map)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

