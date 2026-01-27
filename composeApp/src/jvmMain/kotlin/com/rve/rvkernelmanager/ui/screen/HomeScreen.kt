package com.rve.rvkernelmanager.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Person
import com.rve.rvkernelmanager.ui.components.Card.ItemCard
import com.rve.rvkernelmanager.ui.data.AppIcon
import com.rve.rvkernelmanager.ui.data.HomeItem
import com.rve.rvkernelmanager.ui.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource
import rvkernel_manager_desktop.composeapp.generated.resources.Res
import rvkernel_manager_desktop.composeapp.generated.resources.arch_linux_logo
import rvkernel_manager_desktop.composeapp.generated.resources.cachyos_logo
import rvkernel_manager_desktop.composeapp.generated.resources.fedora_logo
import rvkernel_manager_desktop.composeapp.generated.resources.ic_linux
import rvkernel_manager_desktop.composeapp.generated.resources.linux_mint_logo
import rvkernel_manager_desktop.composeapp.generated.resources.manjaro_logo
import rvkernel_manager_desktop.composeapp.generated.resources.ubuntu_logo

@Composable
fun HomeScreen(
    topBar: @Composable () -> Unit
) {
    val viewModel = HomeViewModel()
    val deviceInfo by viewModel.deviceInfo.collectAsStateWithLifecycle()

    Scaffold(
        topBar = topBar,
    ) { innerPadding ->
        val osIcons = when {
            deviceInfo.os.contains("CachyOS") -> AppIcon.PainterIcon(painterResource(Res.drawable.cachyos_logo))
            deviceInfo.os.contains("Ubuntu") -> AppIcon.PainterIcon(painterResource(Res.drawable.ubuntu_logo))
            deviceInfo.os.contains("Linux Mint") -> AppIcon.PainterIcon(painterResource(Res.drawable.linux_mint_logo))
            deviceInfo.os.contains("Arch Linux") -> AppIcon.PainterIcon(painterResource(Res.drawable.arch_linux_logo))
            deviceInfo.os.contains("Manjaro") -> AppIcon.PainterIcon(painterResource(Res.drawable.manjaro_logo))
            deviceInfo.os.contains("Fedora") -> AppIcon.PainterIcon(painterResource(Res.drawable.fedora_logo))
            else -> AppIcon.PainterIcon(painterResource(Res.drawable.ic_linux))
        }

        val deviceInfoItems = listOf(
            HomeItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Person),
                title = "User",
                summary = deviceInfo.user,
            ),
            HomeItem(
                icon = osIcons,
                title = "Operating system",
                summary = deviceInfo.os
            ),
            HomeItem(
                icon = AppIcon.PainterIcon(painterResource(Res.drawable.ic_linux)),
                title = "Kernel",
                summary = deviceInfo.kernel
            )
        )

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(deviceInfoItems) { item ->
                    ItemCard(
                        icon = item.icon,
                        title = item.title,
                        summary = item.summary,
                    )
                }
            }
        }
    }
}
