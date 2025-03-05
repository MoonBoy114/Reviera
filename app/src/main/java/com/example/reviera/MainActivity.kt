package com.example.reviera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.reviera.data.viewmodel.SpaViewModel
import com.example.reviera.data.viewmodel.SpaViewModelFactory
import com.example.reviera.ui.theme.RivieraSpaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: SpaViewModel by viewModels {
        SpaViewModelFactory((application as SpaApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RivieraSpaTheme {
                SpaApp(viewModel)
            }
        }
    }
}

@Composable
fun SpaApp(viewModel: SpaViewModel) {
    val navController = rememberNavController()
    var selectedNavItem by remember { mutableStateOf("home") }

    // Отслеживание текущего маршрута
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
    LaunchedEffect(currentRoute) {
        selectedNavItem = currentRoute
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel, currentRoute) }
        composable("login") { LoginScreen(navController, viewModel) }
        composable("news") { HomeScreen(navController, viewModel, currentRoute) }
        composable("settings") { SettingsScreen(navController, viewModel, currentRoute) }
        composable("cleaning") { HomeScreen(navController, viewModel, currentRoute) }
        composable("tv") { HomeScreen(navController, viewModel, currentRoute) }
        composable("wifi") { HomeScreen(navController, viewModel, currentRoute) }
        composable("safe") { HomeScreen(navController, viewModel, currentRoute) }
        composable("restaurant") { HomeScreen(navController, viewModel, currentRoute) }
        composable("contacts") { HomeScreen(navController, viewModel, currentRoute) }
        composable("spa") { HomeScreen(navController, viewModel, currentRoute) }
        composable("menu") { HomeScreen(navController, viewModel, currentRoute) }
    }

    LaunchedEffect(Unit) {
        if (!viewModel.isUserLoggedIn()) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        } else {
            navController.navigate("home")
            selectedNavItem = "home" // Устанавливаем "Главная" как активную после логина
            viewModel.loadPromotions()
        }
    }
}