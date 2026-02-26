package com.datec.app.core.router

sealed class AppScreens(val route: String) {
    object Home : AppScreens("home")
}
