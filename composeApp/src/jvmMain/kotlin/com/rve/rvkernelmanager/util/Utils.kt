package com.rve.rvkernelmanager.util

import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object Utils {
    const val TAG = "Utils"
    private val logger = Logger.getLogger(TAG)

    fun getKernelVersion(): String = runCatching {
        File("/proc/version").readText().trim()
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read /proc/version", e)
        "unknown"
    }
}