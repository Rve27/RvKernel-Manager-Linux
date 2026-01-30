package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.cpu.CPUInfo
import com.rve.rvkernelmanager.util.Utils.getCpuFreq
import com.rve.rvkernelmanager.util.Utils.getCpuGovernor
import com.rve.rvkernelmanager.util.Utils.getAvailableCpuFreqs
import com.rve.rvkernelmanager.util.Utils.getAvailableCpuGovernor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CPUViewModel : ViewModel() {
    private val _cpuInfo = MutableStateFlow(CPUInfo())
    val cpuInfo: StateFlow<CPUInfo> = _cpuInfo

    private val _availableFreqs = MutableStateFlow<List<Long>>(emptyList())
    val availableFreqs: StateFlow<List<Long>> = _availableFreqs

    private val _availableGovernors = MutableStateFlow<List<String>>(emptyList())
    val availableGovernors: StateFlow<List<String>> = _availableGovernors


    init {
        getCpuInfo()
    }

    fun getCpuInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _cpuInfo.value = CPUInfo(
                minFreq = getCpuFreq("min"),
                maxFreq = getCpuFreq("max"),
                governor = getCpuGovernor()
            )
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
}