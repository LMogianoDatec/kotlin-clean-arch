package com.datec.app.core.router

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.datec.app.module.presentation.screen.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
        composable(AppScreens.Home.route) {
            HomeScreen()
        }
    }
}
