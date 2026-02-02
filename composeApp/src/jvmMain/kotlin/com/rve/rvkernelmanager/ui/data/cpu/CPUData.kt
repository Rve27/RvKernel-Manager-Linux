package com.rve.rvkernelmanager.ui.data.cpu

data class CPUData(
    val curFreq : Long = 0L,
    val minFreq: Long = 0L,
    val maxFreq: Long = 0L,
    val governor: String = "unknown",
    val hasBoost: Boolean = false,
    val isBoostEnabled: Boolean = false
)
