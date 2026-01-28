package com.rve.rvkernelmanager.ui.data

data class DeviceInfo(
    val user: String = "Rve",
    val hostname: String = "RvEnterprises",
    val os: String = "RvOS",
    val cpu: String = "unknown",
    val gpu: String = "unknown",
    val ram: String = "unknown",
    val isZramActive: Boolean = false,
    val zram: String = "unknown",
    val kernel: String = "RvKernel",
)
