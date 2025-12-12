package com.chelsea.alp_vp_frontend_mentalapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chelsea.alp_vp_frontend_mentalapp.ui.viewmodel.TodoViewModel
import com.chelsea.alp_vp_frontend_mentalapp.ui.view.TodoListScreen
import com.chelsea.alp_vp_frontend_mentalapp.ui.view.AddTaskScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel = TodoViewModel()

    NavHost(
        navController = navController,
        startDestination = "todo"
    ) {
        composable("todo") {
            TodoListScreen(
                viewModel = viewModel,
                onAddTaskClick = { navController.navigate("add") }
            )
        }

        composable("add") {
            AddTaskScreen(
                viewModel = viewModel,
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}