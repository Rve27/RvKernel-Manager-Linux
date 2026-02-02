package com.rve.rvkernelmanager.ui.data.kernel

import com.rve.rvkernelmanager.ui.data.AppIcon

data class KernelItem(
    val icon: AppIcon,
    val title: String,
    val summary: String,
    val onClick: () -> Unit
)
