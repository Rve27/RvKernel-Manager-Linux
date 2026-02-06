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
package com.rve.rvkernelmanager.utils.cpu

import com.rve.rvkernelmanager.utils.Utils
import com.rve.rvkernelmanager.utils.Utils.exec
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object CPUUtils {
    const val TAG = "CPUUtils"
    private val logger = Logger.getLogger(TAG)

    fun getCpuFreq(target: String): Long = runCatching {
        val file = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_${target}_freq")

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

        exec(command)
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to set CPU frequency", e)
        false
    }

    fun getAvailableCpuFreqs(): List<Long> = runCatching {
        val file = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies")

        if (file.exists()) {
            file
                .readText()
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

    fun hasCpuBoost(): Boolean = File("/sys/devices/system/cpu/intel_pstate/no_turbo").exists() ||
        File("/sys/devices/system/cpu/cpufreq/boost").exists()

    fun isCpuBoostEnabled(): Boolean = runCatching {
        val intelNoTurbo = File("/sys/devices/system/cpu/intel_pstate/no_turbo")
        if (intelNoTurbo.exists()) {
            return@runCatching intelNoTurbo.readText().trim() == "0"
        }

        val cpufreqBoost = File("/sys/devices/system/cpu/cpufreq/boost")
        if (cpufreqBoost.exists()) {
            return@runCatching cpufreqBoost.readText().trim() == "1"
        }

        false
    }.getOrElse {
        false
    }

    fun setCpuBoost(enable: Boolean): Boolean = runCatching {
        var command = ""

        if (File("/sys/devices/system/cpu/intel_pstate/no_turbo").exists()) {
            val value = if (enable) "0" else "1"
            command = "echo $value | tee /sys/devices/system/cpu/intel_pstate/no_turbo"
        } else if (File("/sys/devices/system/cpu/cpufreq/boost").exists()) {
            val value = if (enable) "1" else "0"
            command = "echo $value | tee /sys/devices/system/cpu/cpufreq/boost"
        }

        if (command.isNotEmpty()) {
            exec(command)
        } else {
            false
        }
    }.getOrElse {
        logger.log(Level.SEVERE, "Failed to set CPU boost", it)
        false
    }

    fun getCpuGovernor(): String = runCatching {
        File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").readText().trim()
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to read CPU governor", e)
        "unknown"
    }

    fun setCpuGov(governor: String): Boolean = runCatching {
        val command = "echo $governor | tee /sys/devices/system/cpu/cpu*/cpufreq/scaling_governor"
        exec(command)
    }.getOrElse { e ->
        logger.log(Level.SEVERE, "Failed to set CPU governor", e)
        false
    }

    fun getAvailableCpuGovernor(): List<String> = runCatching {
        val file = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors")

        if (file.exists()) {
            file
                .readText()
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
