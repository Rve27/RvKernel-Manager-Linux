package com.rve.rvkernelmanager.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.io.File

data class AppSettings(
    val seedColorArgb: Int = 0xFFEBAC00.toInt(),
    val applyCpuTurboOnBoot: Boolean = false
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

            val hexColorString = getValue("color_scheme")

            val applyCpuTurbo = getValue("apply_cpu_turbo_on_boot")?.toBoolean() ?: false

            val colorInt = if (!hexColorString.isNullOrEmpty()) {
                try {
                    hexColorString.removePrefix("#").toLong(16).toInt()
                } catch (e: Exception) { 0xFFEBAC00.toInt() }
            } else {
                0xFFEBAC00.toInt()
            }

            AppSettings(colorInt, applyCpuTurbo)
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
                appendLine("color_scheme = \"$hexString\"")
                appendLine("apply_cpu_turbo_on_boot = ${settings.applyCpuTurboOnBoot}")
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
        saveConfig(current.copy(applyCpuTurboOnBoot = applyOnBoot))
    }
}