package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.cpu.CPUData
import com.rve.rvkernelmanager.util.Utils.getAvailableCpuFreqs
import com.rve.rvkernelmanager.util.Utils.getAvailableCpuGovernor
import com.rve.rvkernelmanager.util.Utils.getCpuFreq
import com.rve.rvkernelmanager.util.Utils.getCpuGovernor
import com.rve.rvkernelmanager.util.Utils.hasCpuBoost
import com.rve.rvkernelmanager.util.Utils.isCpuBoostEnabled
import com.rve.rvkernelmanager.util.Utils.setCpuBoost
import com.rve.rvkernelmanager.util.Utils.setCpuFreq
import com.rve.rvkernelmanager.util.Utils.setCpuGov
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
            _cpuData.value = CPUData(
                curFreq = getCpuFreq("cur"),
                minFreq = getCpuFreq("min"),
                maxFreq = getCpuFreq("max"),
                governor = getCpuGovernor(),
                hasBoost = hasCpuBoost(),
                isBoostEnabled = isCpuBoostEnabled()
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