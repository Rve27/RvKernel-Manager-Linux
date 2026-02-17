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

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object RootSession {
    private var process: Process? = null
    private var writer: BufferedWriter? = null
    private var reader: BufferedReader? = null
    private var errorReader: BufferedReader? = null

    @Volatile
    private var authenticated = false

    fun isAuthenticated(): Boolean = authenticated

    fun authenticate(password: String): Boolean {
        try {
            destroy()

            val pb = ProcessBuilder("sudo", "-S", "sh")
            pb.redirectErrorStream(false)
            val proc = pb.start()

            val out = BufferedWriter(OutputStreamWriter(proc.outputStream))
            val inp = BufferedReader(InputStreamReader(proc.inputStream))
            val err = BufferedReader(InputStreamReader(proc.errorStream))

            out.write(password)
            out.newLine()
            out.flush()

            // Send a test command and check output to verify authentication
            val marker = "RVKERNEL_AUTH_OK_${System.nanoTime()}"
            out.write("echo $marker")
            out.newLine()
            out.flush()

            val deadline = System.currentTimeMillis() + 5000
            var authSuccess = false

            while (System.currentTimeMillis() < deadline) {
                if (inp.ready()) {
                    val line = inp.readLine()
                    if (line != null && line.contains(marker)) {
                        authSuccess = true
                        break
                    }
                } else {
                    Thread.sleep(100)
                }
            }

            if (authSuccess) {
                process = proc
                writer = out
                reader = inp
                errorReader = err
                authenticated = true
                return true
            } else {
                proc.destroyForcibly()
                return false
            }
        } catch (e: Exception) {
            Log.e("RootSession", "Authentication failed", e)
            return false
        }
    }

    fun exec(command: String): Boolean {
        if (!authenticated || process == null || writer == null) {
            Log.e("RootSession", "Not authenticated, cannot exec: $command")
            return false
        }

        return try {
            val w = writer ?: return false
            val r = reader ?: return false

            val marker = "RVKERNEL_DONE_${System.nanoTime()}"

            w.write(command)
            w.newLine()
            w.write("echo $marker ${'$'}?")
            w.newLine()
            w.flush()

            val deadline = System.currentTimeMillis() + 10000
            var exitCode = -1

            while (System.currentTimeMillis() < deadline) {
                if (r.ready()) {
                    val line = r.readLine()
                    if (line != null && line.startsWith(marker)) {
                        exitCode = line.substringAfter("$marker ").trim().toIntOrNull() ?: -1
                        break
                    }
                } else {
                    Thread.sleep(50)
                }
            }

            exitCode == 0
        } catch (e: Exception) {
            Log.e("RootSession", "Execution failed: $command", e)
            false
        }
    }

    fun destroy() {
        try {
            writer?.let {
                it.write("exit")
                it.newLine()
                it.flush()
            }
        } catch (_: Exception) {}

        try { writer?.close() } catch (_: Exception) {}
        try { reader?.close() } catch (_: Exception) {}
        try { errorReader?.close() } catch (_: Exception) {}
        try { process?.destroyForcibly() } catch (_: Exception) {}

        process = null
        writer = null
        reader = null
        errorReader = null
        authenticated = false
    }
}
