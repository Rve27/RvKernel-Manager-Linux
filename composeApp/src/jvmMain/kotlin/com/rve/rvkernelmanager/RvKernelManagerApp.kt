package com.rve.rvkernelmanager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rve.rvkernelmanager.ui.components.AppBar.SimpleTopAppBar
import com.rve.rvkernelmanager.ui.components.Navigation.BottomNavigationBar
import com.rve.rvkernelmanager.ui.screen.DummyScreen
import com.rve.rvkernelmanager.ui.screen.HomeScreen
import com.rve.rvkernelmanager.ui.theme.RvKernelManagerTheme
import com.rve.rvkernelmanager.ui.components.Navigation.Home
import com.rve.rvkernelmanager.ui.components.Navigation.CPU
import com.rve.rvkernelmanager.ui.components.Navigation.Kernel

@Composable
fun RvKernelManagerApp() {
    val navController = rememberNavController()

    RvKernelManagerTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = { SimpleTopAppBar() },
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Home> {
                        HomeScreen()
                    }
                    composable<CPU> {
                        DummyScreen()
                    }
                    composable<Kernel> {
                        DummyScreen()
                    }
                }
            }

            BottomNavigationBar(
                navController = navController,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}
