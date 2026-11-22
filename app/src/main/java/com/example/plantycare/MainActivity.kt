package com.example.plantycare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.plantycare.ui.navigation.NavGraph
import com.example.plantycare.ui.theme.CreamBackground
import com.example.plantycare.ui.theme.PlantyCareTheme
import com.example.plantycare.ui.viewmodel.PlantViewModel
import com.example.plantycare.ui.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {

    private val plantViewModel: PlantViewModel by viewModels {
        val app = application as PlantyCareApp
        PlantViewModel.Factory(app.plantRepository, app.sensorRepository)
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        val app = application as PlantyCareApp
        ProfileViewModel.Factory(app.plantRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlantyCareTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CreamBackground
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        plantViewModel = plantViewModel,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }
}
