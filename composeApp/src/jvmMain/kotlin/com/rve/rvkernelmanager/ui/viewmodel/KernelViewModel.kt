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
            _uclampData.value =
                UclampData(
                    hasUclamp = hasUclamp(),
                    max = getUclamp("max"),
                    min = getUclamp("min"),
                    minRt = getUclamp("min_rt_default"),
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
