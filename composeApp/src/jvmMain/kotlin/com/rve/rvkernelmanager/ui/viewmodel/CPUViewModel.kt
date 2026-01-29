package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.cpu.CPUInfo
import com.rve.rvkernelmanager.util.Utils.getCpuFreq
import com.rve.rvkernelmanager.util.Utils.getCpuGovernor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CPUViewModel : ViewModel() {
    private val _cpuInfo = MutableStateFlow(CPUInfo())
    val cpuInfo: StateFlow<CPUInfo> = _cpuInfo

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
}