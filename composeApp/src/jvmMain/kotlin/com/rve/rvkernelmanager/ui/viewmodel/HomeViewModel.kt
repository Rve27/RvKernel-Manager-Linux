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

package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.DeviceInfo
import com.rve.rvkernelmanager.utils.Utils.getCpuModel
import com.rve.rvkernelmanager.utils.Utils.getGpuModel
import com.rve.rvkernelmanager.utils.Utils.getHostname
import com.rve.rvkernelmanager.utils.Utils.getKernelVersion
import com.rve.rvkernelmanager.utils.Utils.getOS
import com.rve.rvkernelmanager.utils.Utils.getRamStatus
import com.rve.rvkernelmanager.utils.Utils.getTotalSwap
import com.rve.rvkernelmanager.utils.Utils.getTotalZram
import com.rve.rvkernelmanager.utils.Utils.getUsername
import com.rve.rvkernelmanager.utils.Utils.isSwapActive
import com.rve.rvkernelmanager.utils.Utils.isZramActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {
    val deviceInfo: StateFlow<DeviceInfo> = flow {
        val staticData = DeviceInfo(
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
            ram = getRamStatus()
        )

        emit(staticData)

        while (true) {
            delay(3000)

            val updatedData = staticData.copy(
                ram = getRamStatus()
            )

            emit(updatedData)
        }
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DeviceInfo()
        )
}