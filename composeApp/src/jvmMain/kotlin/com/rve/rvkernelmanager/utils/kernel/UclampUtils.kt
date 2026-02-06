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
package com.rve.rvkernelmanager.utils.kernel

import com.rve.rvkernelmanager.utils.Utils
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

object UclampUtils {
    const val TAG = "UclampUtils"
    private val logger = Logger.getLogger(TAG)

    fun hasUclamp(): Boolean = File("/proc/sys/kernel/sched_util_clamp_min").exists()

    fun getUclamp(target: String): Int = runCatching {
        val file = File("/proc/sys/kernel/sched_util_clamp_$target")

        if (file.exists()) {
            file.readText().trim().toIntOrNull() ?: 0
        } else {
            0
        }
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
