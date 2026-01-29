package com.rve.rvkernelmanager.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.roundedfilled.Manufacturing
import com.composables.icons.materialsymbols.roundedfilled.Memory
import com.composables.icons.materialsymbols.roundedfilled.Speed
import com.rve.rvkernelmanager.ui.components.List.ListItem
import com.rve.rvkernelmanager.ui.data.AppIcon
import com.rve.rvkernelmanager.ui.data.cpu.CPUItem
import com.rve.rvkernelmanager.ui.viewmodel.CPUViewModel

@Composable
fun CPUScreen() {
    val viewModel = CPUViewModel()
    val cpuInfo by viewModel.cpuInfo.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        val cpuItems = listOf(
            CPUItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Minimum frequency",
                summary = "${cpuInfo.minFreq} MHz",
                onClick = { }
            ),
            CPUItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Speed),
                title = "Maximum frequency",
                summary = "${cpuInfo.maxFreq} MHz",
                onClick = { }
            ),
            CPUItem(
                icon = AppIcon.ImageVectorIcon(MaterialSymbols.RoundedFilled.Manufacturing),
                title = "Governor",
                summary = cpuInfo.governor,
                onClick = { }
            ),
        )

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Card(
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                LazyColumn {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        imageVector = MaterialSymbols.RoundedFilled.Memory,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                                        contentDescription = null
                                    )
                                }
                                Text(
                                    text = "CPU",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    items(cpuItems) { item ->
                        ListItem(
                            icon = item.icon,
                            title = item.title,
                            summary = item.summary,
                            onClick = item.onClick
                        )
                    }
                }
            }
        }
    }
}