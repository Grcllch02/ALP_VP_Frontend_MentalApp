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
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chelsea.alp_vp_frontend_mentalapp.ui.auth.AuthViewModel
import com.chelsea.alp_vp_frontend_mentalapp.ui.auth.LoginScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.auth.RegisterScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.focus.FocusScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.focus.FocusViewModel
import com.chelsea.alp_vp_frontend_mentalapp.ui.screens.GameScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.screens.SettingsScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.theme.ALP_VP_Frontend_MentalAppTheme
import com.chelsea.alp_vp_frontend_mentalapp.ui.todo.TodoDetailScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.todo.TodoScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.todo.TodoViewModel

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
    // 1. Inisialisasi Semua ViewModel di sini (Shared)
    val authViewModel: AuthViewModel = viewModel()
    val focusViewModel: FocusViewModel = viewModel()
    val todoViewModel: TodoViewModel = viewModel() // ViewModel Todo List

    // 2. State untuk Login/Register
    var showLoginScreen by remember { mutableStateOf(false) } // Ubah ke true jika ingin start dari login
    var showRegisterScreen by remember { mutableStateOf(false) }

    // 3. Setup Navigasi (Pengganti 'when' biasa)
    val navController = rememberNavController()
    // State untuk mengatur warna tombol bottom bar agar aktif
    val selectedTab = remember { mutableIntStateOf(0) }

    when {
        showLoginScreen -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    showLoginScreen = false
                    // Reset ke halaman awal (Todo)
                    selectedTab.intValue = 0
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
                    selectedTab.intValue = 0
                },
                onNavigateToLogin = {
                    showRegisterScreen = false
                    showLoginScreen = true
                }
            )
        }
        else -> {
            // HALAMAN UTAMA (Scaffold dengan Bottom Bar)
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        // TAB 0: Todo List
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.List, contentDescription = "Todo") },
                            label = { Text("Todo") },
                            selected = selectedTab.intValue == 0,
                            onClick = {
                                selectedTab.intValue = 0
                                navController.navigate("todo_list") {
                                    popUpTo("todo_list") { inclusive = true }
                                }
                            }
                        )

                        // TAB 1: Focus Timer
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Timer, contentDescription = "Timer") },
                            label = { Text("Timer") },
                            selected = selectedTab.intValue == 1,
                            onClick = {
                                selectedTab.intValue = 1
                                navController.navigate("timer") {
                                    popUpTo("todo_list")
                                }
                            }
                        )

                        // TAB 2: Game
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Game") },
                            label = { Text("Game") },
                            selected = selectedTab.intValue == 2,
                            onClick = {
                                selectedTab.intValue = 2
                                navController.navigate("game") {
                                    popUpTo("todo_list")
                                }
                            }
                        )

                        // TAB 3: Settings
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                            label = { Text("Settings") },
                            selected = selectedTab.intValue == 3,
                            onClick = {
                                selectedTab.intValue = 3
                                navController.navigate("settings") {
                                    popUpTo("todo_list")
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // 4. NAV HOST (Pengatur Lalu Lintas Halaman)
                    NavHost(
                        navController = navController,
                        startDestination = "todo_list"
                    ) {
                        // --- RUTE TODO LIST ---
                        composable("todo_list") {
                            TodoScreen(
                                viewModel = todoViewModel,
                                onNavigateToDetail = { taskId ->
                                    // Pindah ke halaman detail edit
                                    navController.navigate("todo_detail/$taskId")
                                }
                            )
                        }
                        composable(
                            route = "todo_detail/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
                            TodoDetailScreen(
                                taskId = taskId,
                                viewModel = todoViewModel,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // --- RUTE TIMER ---
                        composable("timer") {
                            FocusScreen(viewModel = focusViewModel)
                        }

                        // --- RUTE GAME ---
                        composable("game") {
                            GameScreen(userId = authViewModel.currentUser.value?.id)
                        }

                        // --- RUTE SETTINGS ---
                        composable("settings") {
                            SettingsScreen(
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
}