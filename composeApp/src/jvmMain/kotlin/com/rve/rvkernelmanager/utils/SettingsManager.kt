package com.rve.rvkernelmanager.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AppSettings(
    val seedColorArgb: Int = 0xFFEBAC00.toInt()
)

object SettingsManager {
    private val configDir = File(System.getProperty("user.home"), ".config/rvkernel-manager")
    private val configFile = File(configDir, "settings.json")

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    fun loadSettings(): AppSettings {
        return try {
            if (configFile.exists()) {
                val content = configFile.readText()
                json.decodeFromString<AppSettings>(content)
            } else {
                AppSettings()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AppSettings()
        }
    }

    fun saveColor(color: Color) {
        try {
            if (!configDir.exists()) {
                configDir.mkdirs()
            }
            val currentSettings = loadSettings()
            val newSettings = currentSettings.copy(seedColorArgb = color.toArgb())

            configFile.writeText(json.encodeToString(AppSettings.serializer(), newSettings))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}