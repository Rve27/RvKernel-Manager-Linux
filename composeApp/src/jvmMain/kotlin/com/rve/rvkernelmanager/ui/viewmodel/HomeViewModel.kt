package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.DeviceInfo
import com.rve.rvkernelmanager.util.Utils.getCpuModel
import com.rve.rvkernelmanager.util.Utils.getGpuModel
import com.rve.rvkernelmanager.util.Utils.getHostname
import com.rve.rvkernelmanager.util.Utils.getKernelVersion
import com.rve.rvkernelmanager.util.Utils.getOS
import com.rve.rvkernelmanager.util.Utils.getTotalRam
import com.rve.rvkernelmanager.util.Utils.getUsername
import com.rve.rvkernelmanager.util.Utils.isZramActive
import com.rve.rvkernelmanager.util.Utils.getTotalZram
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _deviceInfo = MutableStateFlow(DeviceInfo())
    val deviceInfo: StateFlow<DeviceInfo> = _deviceInfo

    init {
        getDeviceInfo()
    }

    fun getDeviceInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _deviceInfo.value = DeviceInfo(
                user = getUsername(),
                hostname = getHostname(),
                os = getOS(),
                cpu = getCpuModel(),
                gpu = getGpuModel(),
                ram = getTotalRam(),
                isZramActive = isZramActive(),
                zram = getTotalZram(),
                kernel = getKernelVersion()
            )
        }
    }
}