package com.chelsea.alp_vp_frontend_mentalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
<<<<<<< Updated upstream
=======
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
>>>>>>> Stashed changes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chelsea.alp_vp_frontend_mentalapp.ui.navigation.NavGraph
import com.chelsea.alp_vp_frontend_mentalapp.ui.theme.ALP_VP_Frontend_MentalAppTheme
import com.chelsea.alp_vp_frontend_mentalapp.ui.todo.TodoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ALP_VP_Frontend_MentalAppTheme {
                NavGraph()
            }
        }
    }
}

@Composable
<<<<<<< Updated upstream
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
=======
fun MainApp() {
    val selectedTab = remember { mutableIntStateOf(1) }
    val authViewModel: AuthViewModel = viewModel()
    val focusViewModel: FocusViewModel = viewModel()  // ← TAMBAHKAN INI

    var showLoginScreen by remember { mutableStateOf(false) }
    var showRegisterScreen by remember { mutableStateOf(false) }

    when {
        showLoginScreen -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    showLoginScreen = false
                    selectedTab.intValue = 2
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
                    selectedTab.intValue = 2
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
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.StickyNote2, contentDescription = "Todo List") },
                            label = { Text("Todo") },
                            selected = selectedTab.intValue == 0,
                            onClick = { selectedTab.intValue = 0 }
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Timer, contentDescription = "Focus Timer") },
                            label = { Text("Timer") },
                            selected = selectedTab.intValue == 1,
                            onClick = { selectedTab.intValue = 1 }
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "BlockBlast Game") },
                            label = { Text("Game") },
                            selected = selectedTab.intValue == 2,
                            onClick = { selectedTab.intValue = 2 }
                        )

                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                            label = { Text("Settings") },
                            selected = selectedTab.intValue == 3,
                            onClick = { selectedTab.intValue = 3 }
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
                        0 -> TodoScreen()
                        1 -> FocusScreen(viewModel = focusViewModel)  // ← Pass ViewModel
                        2 -> GameScreen(userId = authViewModel.currentUser.value?.id)
                        3 -> SettingsScreen(
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
>>>>>>> Stashed changes
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ALP_VP_Frontend_MentalAppTheme {
        Greeting("Android")
    }
}