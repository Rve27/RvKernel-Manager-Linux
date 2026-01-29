package com.rve.rvkernelmanager.ui.data.cpu

import com.rve.rvkernelmanager.ui.data.AppIcon

data class CPUItem(
    val icon: AppIcon,
    val title: String,
    val summary: String,
    val onClick: () -> Unit
)
