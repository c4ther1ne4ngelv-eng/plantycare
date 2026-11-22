package com.example.plantycare.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Home : Screen("home")
    object PlantDetail : Screen("plant_detail/{plantId}") {
        fun createRoute(plantId: Long) = "plant_detail/$plantId"
    }
    object AddPlant : Screen("add_plant")
    object Profile : Screen("profile")
    object History : Screen("history")
}
