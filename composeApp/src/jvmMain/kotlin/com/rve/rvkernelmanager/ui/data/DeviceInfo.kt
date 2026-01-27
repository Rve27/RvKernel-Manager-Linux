package com.rve.rvkernelmanager.ui.data

data class DeviceInfo(
    val user: String = "Rve",
    val hostname: String = "RvEnterprises",
    val os: String = "RvOS",
    val cpu: String = "RvCPU",
    val gpu: String = "RvGPU",
    val ram: String = "RvRAM",
    val kernel: String = "RvKernel",
)
