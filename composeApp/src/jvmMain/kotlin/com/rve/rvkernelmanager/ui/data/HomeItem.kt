package com.rve.rvkernelmanager.ui.data

import androidx.compose.runtime.MutableState

data class HomeItem(
    val icon: Unit,
    val title: String,
    val summary: MutableState<String>,
)
