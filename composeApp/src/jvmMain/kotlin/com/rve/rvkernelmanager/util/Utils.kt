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

    fun getCpuFreq(target: String): Long = runCatching {
        val fileName = when (target.lowercase()) {
            "max" -> "scaling_max_freq"
            "min" -> "scaling_min_freq"
            else -> return@runCatching 0L
        }

        val file = File("/sys/devices/system/cpu/cpu0/cpufreq/$fileName")

        if (file.exists()) {
            val freqKHz = file.readText().trim().toLong()
            freqKHz / 1000
        } else {
            0L
        }
    }.getOrElse { e ->
        logger.log(Level.WARNING, "Failed to read CPU $target freq", e)
        0L
    }

    fun setCpuFreq(freqMHz: Long, isMax: Boolean): Boolean = runCatching {
        val type = if (isMax) "max" else "min"
        val fileName = "scaling_${type}_freq"
        val freqKHz = freqMHz * 1000

        val command = "echo $freqKHz | tee /sys/devices/system/cpu/cpu*/cpufreq/$fileName"

        val process = ProcessBuilder(
            "pkexec", "sh", "-c", command
        ).start()

        val exitCode = process.waitFor()
        exitCode == 0
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to set CPU frequency", e)
        false
    }

    fun getAvailableCpuFreqs(): List<Long> = runCatching {
        val file = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies")

        if (file.exists()) {
            file.readText()
                .trim()
                .split(Regex("\\s+"))
                .mapNotNull { it.toLongOrNull() }
                .map { it / 1000 }
                .sorted()
        } else {
            emptyList()
        }
    }.getOrElse { e ->
        logger.log(Level.WARNING, "Failed to read available CPU frequencies", e)
        emptyList()
    }

    fun getCpuGovernor(): String = runCatching {
        File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").readText().trim()
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read CPU governor", e)
        "unknown"
    }

    fun getAvailableCpuGovernor(): List<String> = runCatching {
        val file = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors")

        if (file.exists()) {
            file.readText()
                .trim()
                .split(Regex("\\s+"))
                .sorted()
        } else {
            emptyList()
        }
    }.getOrElse { e ->
        logger.log(Level.WARNING, "Failed to read available CPU governor", e)
        emptyList()
    }
}