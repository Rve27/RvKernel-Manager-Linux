package com.rve.rvkernelmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rve.rvkernelmanager.ui.components.Card.ItemCard
import com.rve.rvkernelmanager.ui.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource
import rvkernel_manager_desktop.composeapp.generated.resources.Res
import rvkernel_manager_desktop.composeapp.generated.resources.ic_linux

@Composable
fun HomeScreen(
    topBar: @Composable () -> Unit
) {
    val viewModel = HomeViewModel()
    val deviceInfo by viewModel.deviceInfo.collectAsStateWithLifecycle()

    Scaffold(
        topBar = topBar,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
            ) {
                item {
                    ItemCard(
                        icon = painterResource(Res.drawable.ic_linux),
                        title = "Kernel version",
                        summary = deviceInfo.kernel
                    )
                }
            }
        }
    }
}