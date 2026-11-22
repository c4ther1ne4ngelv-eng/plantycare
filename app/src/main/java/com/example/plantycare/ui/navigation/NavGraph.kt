package com.example.plantycare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.plantycare.ui.screens.add.AddPlantScreen
import com.example.plantycare.ui.screens.auth.AuthScreen
import com.example.plantycare.ui.screens.detail.PlantDetailScreen
import com.example.plantycare.ui.screens.history.HistoryScreen
import com.example.plantycare.ui.screens.home.HomeScreen
import com.example.plantycare.ui.screens.profile.ProfileScreen
import com.example.plantycare.ui.screens.splash.SplashScreen
import com.example.plantycare.ui.viewmodel.PlantViewModel
import com.example.plantycare.ui.viewmodel.ProfileViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    plantViewModel: PlantViewModel,
    profileViewModel: ProfileViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. APPLE-STYLE SPLASH SCREEN
        composable(Screen.Splash.route) {
            SplashScreen(
                profileViewModel = profileViewModel,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. AUTH SCREEN (LOGIN & SIGN UP)
        composable(Screen.Auth.route) {
            AuthScreen(
                profileViewModel = profileViewModel,
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. HOME DASHBOARD
        composable(Screen.Home.route) {
            HomeScreen(
                plantViewModel = plantViewModel,
                profileViewModel = profileViewModel,
                onNavigateToDetail = { plantId ->
                    navController.navigate(Screen.PlantDetail.createRoute(plantId))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.AddPlant.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        // 4. PLANT DETAIL (WITH FIX FOR RECOMPOSITION JITTER)
        composable(
            route = Screen.PlantDetail.route,
            arguments = listOf(navArgument("plantId") { type = NavType.LongType })
        ) { backStackEntry ->
            val plantId = backStackEntry.arguments?.getLong("plantId") ?: 0L
            PlantDetailScreen(
                plantId = plantId,
                plantViewModel = plantViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 5. ADD PLANT (RICH CATALOG ENCYCLOPEDIA)
        composable(Screen.AddPlant.route) {
            AddPlantScreen(
                plantViewModel = plantViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 6. USER PROFILE (READ-ONLY WITH PENCIL EDIT ICON)
        composable(Screen.Profile.route) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogoutSuccess = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 7. CARE HISTORY LOGS
        composable(Screen.History.route) {
            HistoryScreen(
                plantViewModel = plantViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
