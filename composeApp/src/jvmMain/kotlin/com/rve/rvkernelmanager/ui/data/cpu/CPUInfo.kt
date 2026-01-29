package com.rve.rvkernelmanager.ui.data.cpu

data class CPUInfo(
    val minFreq: Long = 0L,
    val maxFreq: Long = 0L,
    val governor: String = "unknown"
)
