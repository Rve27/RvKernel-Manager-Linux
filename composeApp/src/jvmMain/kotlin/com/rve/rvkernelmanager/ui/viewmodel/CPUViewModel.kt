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
import com.rve.rvkernelmanager.ui.data.cpu.CPUData
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.getAvailableCpuFreqs
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.getAvailableCpuGovernor
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.getCpuFreq
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.getCpuGovernor
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.hasCpuBoost
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.isCpuBoostEnabled
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.setCpuBoost
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.setCpuFreq
import com.rve.rvkernelmanager.utils.cpu.CPUUtils.setCpuGov
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CPUViewModel : ViewModel() {
    private val _cpuData = MutableStateFlow(CPUData())
    val cpuData: StateFlow<CPUData> = _cpuData

    private val _availableFreqs = MutableStateFlow<List<Long>>(emptyList())
    val availableFreqs: StateFlow<List<Long>> = _availableFreqs

    private val _availableGovernors = MutableStateFlow<List<String>>(emptyList())
    val availableGovernors: StateFlow<List<String>> = _availableGovernors

    init {
        getCpuData()
        updateCurFreq()
    }

    fun getCpuData() {
        viewModelScope.launch(Dispatchers.IO) {
            _cpuData.value =
                CPUData(
                    curFreq = getCpuFreq("cur"),
                    minFreq = getCpuFreq("min"),
                    maxFreq = getCpuFreq("max"),
                    governor = getCpuGovernor(),
                    hasBoost = hasCpuBoost(),
                    isBoostEnabled = isCpuBoostEnabled(),
                )
        }
    }

    fun updateCurFreq() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(3000)
                val currentFreq = getCpuFreq("cur")
                _cpuData.update { currentState ->
                    currentState.copy(curFreq = currentFreq)
                }
            }
        }
    }

    fun getAvailableFreqs() {
        viewModelScope.launch(Dispatchers.IO) {
            _availableFreqs.value = getAvailableCpuFreqs()
        }
    }

    fun getAvailableGovernors() {
        viewModelScope.launch(Dispatchers.IO) {
            _availableGovernors.value = getAvailableCpuGovernor()
        }
    }

    fun setCpuGovernor(governor: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = setCpuGov(governor)
            if (success) {
                _cpuData.update { currentState ->
                    currentState.copy(governor = governor)
                }
            }
        }
    }

    fun setCpuFrequency(freq: Long, isMax: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = setCpuFreq(freq, isMax)
            if (success) {
                if (isMax) {
                    _cpuData.update { currentState ->
                        currentState.copy(maxFreq = freq)
                    }
                } else {
                    _cpuData.update { currentState ->
                        currentState.copy(minFreq = freq)
                    }
                }
            }
        }
    }

    fun toggleCpuBoost(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = setCpuBoost(enable)
            if (success) {
                _cpuData.update { currentState ->
                    currentState.copy(isBoostEnabled = enable)
                }
            }
        }
    }
}
