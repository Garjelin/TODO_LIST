package com.example.todo_list.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo_list.ui.TaskDetailScreen
import com.example.todo_list.ui.TaskListScreen
import android.util.Log

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "task_list"
    ) {
        composable("task_list") {
            TaskListScreen(
                onTaskClick = { taskId ->
                    navController.navigate("task_detail/$taskId") {
                        Log.d("LOG_MSG", "Navigating to task_detail with ID: $taskId")
                    }
                }
            )
        }
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            TaskDetailScreen(
                taskId = taskId,
                onBackClick = {
                    navController.popBackStack()
                    Log.d("LOG_MSG", "popBackStack")
                }
            )
        }
    }
}