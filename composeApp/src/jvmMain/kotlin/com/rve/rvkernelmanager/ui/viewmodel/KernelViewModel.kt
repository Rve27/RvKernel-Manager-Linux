package com.rve.rvkernelmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rve.rvkernelmanager.ui.data.kernel.UclampData
import com.rve.rvkernelmanager.utils.kernel.UclampUtils.getUclamp
import com.rve.rvkernelmanager.utils.kernel.UclampUtils.hasUclamp
import com.rve.rvkernelmanager.utils.kernel.UclampUtils.setUclamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KernelViewModel : ViewModel() {
    private val _uclampData = MutableStateFlow(UclampData())
    val uclampData: StateFlow<UclampData> = _uclampData

    init {
        getKernelData()
    }

    fun getKernelData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uclampData.value = UclampData(
                hasUclamp = hasUclamp(),
                max = getUclamp("max"),
                min = getUclamp("min"),
                minRt = getUclamp("min_rt_default")
            )
        }
    }

    fun setUclampValue(target: String, value: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = setUclamp(target, value)

            if (success) {
                _uclampData.update { currentState ->
                    when (target) {
                        "max" -> currentState.copy(max = value)
                        "min" -> currentState.copy(min = value)
                        "min_rt_default" -> currentState.copy(minRt = value)
                        else -> currentState
                    }
                }
            }
        }
    }
}