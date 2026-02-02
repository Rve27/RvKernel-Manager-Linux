package com.rve.rvkernelmanager.utils.kernel

import com.rve.rvkernelmanager.utils.Utils
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object UclampUtils {
    const val TAG = "UclampUtils"
    private val logger = Logger.getLogger(TAG)

    fun getUclamp(target: String): Int = runCatching {
        val file = File("/proc/sys/kernel/sched_util_clamp_$target")

        if (file.exists()) {
            file.readText().trim().toIntOrNull() ?: 0
        } else 0
    }.getOrElse { e ->
        logger.log(Level.WARNING, "Failed to read uclamp param: $target", e)
        0
    }

    fun setUclamp(target: String, value: Int): Boolean = runCatching {
        val command = "echo $value | tee /proc/sys/kernel/sched_util_clamp_$target"

        Utils.exec(command)

    }.getOrElse {
        logger.log(Level.SEVERE, "Failed to set uclamp param: $target", it)
        false
    }
}