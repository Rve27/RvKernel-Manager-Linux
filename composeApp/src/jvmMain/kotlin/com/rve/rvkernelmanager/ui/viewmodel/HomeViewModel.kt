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
package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.DeviceInfo
import com.rve.rvkernelmanager.utils.Utils.getCpuModel
import com.rve.rvkernelmanager.utils.Utils.getFreeRam
import com.rve.rvkernelmanager.utils.Utils.getGpuModel
import com.rve.rvkernelmanager.utils.Utils.getHostname
import com.rve.rvkernelmanager.utils.Utils.getKernelVersion
import com.rve.rvkernelmanager.utils.Utils.getOS
import com.rve.rvkernelmanager.utils.Utils.getTotalRam
import com.rve.rvkernelmanager.utils.Utils.getTotalSwap
import com.rve.rvkernelmanager.utils.Utils.getTotalZram
import com.rve.rvkernelmanager.utils.Utils.getUsedRam
import com.rve.rvkernelmanager.utils.Utils.getUsername
import com.rve.rvkernelmanager.utils.Utils.isSwapActive
import com.rve.rvkernelmanager.utils.Utils.isZramActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _deviceInfo = MutableStateFlow(DeviceInfo())
    val deviceInfo: StateFlow<DeviceInfo> = _deviceInfo

    private var ramJob: Job? = null

    init {
        getDeviceInfo()
    }

    private fun getDeviceInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _deviceInfo.value = DeviceInfo(
                user = getUsername(),
                hostname = getHostname(),
                os = getOS(),
                cpu = getCpuModel(),
                gpu = getGpuModel(),
                isZramActive = isZramActive(),
                zram = getTotalZram(),
                isSwapActive = isSwapActive(),
                swap = getTotalSwap(),
                kernel = getKernelVersion(),
                ramTotal = getTotalRam(),
                ramUsed = getUsedRam(),
                ramFree = getFreeRam(),
            )
        }
    }

    fun updateRamStatus() {
        ramJob?.cancel()

        ramJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(3000)
                val currentRamTotal = getTotalRam()
                val currentRamUsed = getUsedRam()
                val currentRamFree = getFreeRam()
                _deviceInfo.update { currentState ->
                    currentState.copy(
                        ramTotal = currentRamTotal,
                        ramUsed = currentRamUsed,
                        ramFree = currentRamFree,
                    )
                }
            }
        }
    }

    fun stopRamJob() {
        ramJob?.cancel()
        ramJob = null
    }
}
