package com.chelsea.alp_vp_frontend_mentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chelsea.alp_vp_frontend_mentalapp.ui.auth.AuthViewModel
import com.chelsea.alp_vp_frontend_mentalapp.ui.auth.LoginScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.auth.RegisterScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.screens.GameScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.screens.SettingsScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.theme.ALP_VP_Frontend_MentalAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ALP_VP_Frontend_MentalAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val selectedTab = remember { mutableIntStateOf(1) } // Default to middle tab (game)
    val authViewModel: AuthViewModel = viewModel()
    var showLoginScreen by remember { mutableStateOf(false) }
    var showRegisterScreen by remember { mutableStateOf(false) }

    // Show login or register screen if navigating from settings
    when {
        showLoginScreen -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    showLoginScreen = false
                    selectedTab.intValue = 2 // Go back to settings
                },
                onNavigateToRegister = {
                    showLoginScreen = false
                    showRegisterScreen = true
                }
            )
        }
        showRegisterScreen -> {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    showRegisterScreen = false
                    selectedTab.intValue = 2 // Go back to settings
                },
                onNavigateToLogin = {
                    showRegisterScreen = false
                    showLoginScreen = true
                }
            )
        }
        else -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        // Left section
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Todo List") },
                            label = { Text("Todo") },
                            selected = selectedTab.intValue == 0,
                            onClick = { selectedTab.intValue = 0 }
                        )

                        // Middle section - Game
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "BlockBlast Game") },
                            label = { Text("Game") },
                            selected = selectedTab.intValue == 1,
                            onClick = { selectedTab.intValue = 1 }
                        )

                        // Right section
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                            label = { Text("Settings") },
                            selected = selectedTab.intValue == 2,
                            onClick = { selectedTab.intValue = 2 }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    when (selectedTab.intValue) {
                        0 -> PlaceholderScreen("Todo List")
                        1 -> GameScreen(userId = authViewModel.currentUser.value?.id)
                        2 -> SettingsScreen(
                            authViewModel = authViewModel,
                            onNavigateToLogin = { showLoginScreen = true },
                            onNavigateToRegister = { showRegisterScreen = true }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Text(text = "$title - Coming Soon", Modifier.padding())
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    ALP_VP_Frontend_MentalAppTheme {
        MainApp()
    }
}