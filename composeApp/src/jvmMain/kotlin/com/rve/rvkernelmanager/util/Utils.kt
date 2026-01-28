package com.rve.rvkernelmanager.util

import java.io.File
import java.text.DecimalFormat
import java.util.logging.Level
import java.util.logging.Logger

object Utils {
    const val TAG = "Utils"
    private val logger = Logger.getLogger(TAG)

    fun getUsername(): String = runCatching {
        System.getProperty("user.name")
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to get user name", e)
        "unknown"
    }

    fun getHostname(): String = runCatching {
        File("/etc/hostname").readText().trim()
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read /etc/hostname", e)
        "unknown"
    }

    fun getOS(): String = runCatching {
        File("/etc/os-release").useLines { lines ->
            lines.find { it.startsWith("PRETTY_NAME=") }
                ?.substringAfter("=")
                ?.replace("\"", "")
                ?: "unknown"
        }
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read /etc/os-release", e)
        "unknown"
    }

    fun getCpuModel(): String = runCatching {
        File("/proc/cpuinfo").useLines { seq ->
            val lines = seq.toList()
            val model = lines.find { it.startsWith("model name") }
                ?.substringAfter(":")
                ?.trim()
                ?: "unknown"
            val coreCount = lines.count { it.startsWith("processor") }
            "$model ($coreCount cores)"
        }
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read CPU info", e)
        "unknown"
    }

    fun getGpuModel(): String = runCatching {
        val process = ProcessBuilder(
            "bash", "-c",
            "lspci | grep -i 'vga\\|3d\\|display' | cut -d ':' -f3 | head -n 1"
        ).start()

        val text = process.inputStream.bufferedReader().readText().trim()

        text.ifBlank { "unknown" }
    }.getOrElse { e ->
        logger.log(Level.WARNING, "Failed to get GPU info", e)
        "unknown"
    }

    fun getTotalRam(): String = runCatching {
        File("/proc/meminfo").useLines { lines ->
            val memTotalLine = lines.find { it.startsWith("MemTotal:") }
            val kbValue = memTotalLine?.replace(Regex("\\D+"), "")?.toLong() ?: 0L

            if (kbValue > 0) {
                val gbValue = kbValue / (1024.0 * 1024.0)
                val df = DecimalFormat("#.#")
                "${df.format(gbValue)} GB"
            } else {
                "unknown"
            }
        }
    }.getOrElse { e ->
        logger.log(Level.WARNING, "Failed to get RAM info", e)
        "unknown"
    }

    fun isZramActive(): Boolean {
        val zramDir = File("/sys/block/")
        return zramDir.exists() && (zramDir.listFiles()?.any { it.name.startsWith("zram") } == true)
    }

    fun getTotalZram(): String = runCatching {
        val zramDir = File("/sys/block/")
        val zramFiles = zramDir.listFiles { _, name -> name.startsWith("zram") }

        if (zramFiles.isNullOrEmpty()) return "unknown"

        val firstDevice = zramFiles.first()
        val algoFile = File(firstDevice, "comp_algorithm")
        val algorithm = if (algoFile.exists()) {
            val content = algoFile.readText()
            Regex("\\[(.*?)]").find(content)?.groupValues?.get(1) ?: "Unknown"
        } else {
            "unknown"
        }

        val totalBytes = zramFiles.sumOf { deviceDir ->
            val sizeFile = File(deviceDir, "disksize")
            if (sizeFile.exists()) {
                sizeFile.readText().trim().toLongOrNull() ?: 0L
            } else {
                0L
            }
        }

        if (totalBytes > 0) {
            val gbValue = totalBytes / (1024.0 * 1024.0 * 1024.0)
            val df = DecimalFormat("#.#")
            "$algorithm (${df.format(gbValue)} GB)"
        } else {
            "unknown"
        }
    }.getOrElse {
        "unknown"
    }

    fun isSwapActive(): Boolean = runCatching {
        File("/proc/meminfo").useLines { lines ->
            val swapTotalLine = lines.find { it.startsWith("SwapTotal:") }
            val totalKb = swapTotalLine?.replace(Regex("\\D+"), "")?.toLongOrNull() ?: 0L
            totalKb > 0
        }
    }.getOrElse { false }

    fun getTotalSwap(): String = runCatching {
        File("/proc/meminfo").useLines { lines ->
            val swapTotalLine = lines.find { it.startsWith("SwapTotal:") }
            val kbValue = swapTotalLine?.replace(Regex("\\D+"), "")?.toLong() ?: 0L

            if (kbValue > 0) {
                val gbValue = kbValue / (1024.0 * 1024.0)
                val df = DecimalFormat("#.#")
                "${df.format(gbValue)} GB"
            } else {
                "unknown"
            }
        }
    }.getOrElse {
        "unknown"
    }

    fun getKernelVersion(): String = runCatching {
        File("/proc/version").readText().trim()
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read /proc/version", e)
        "unknown"
    }
}