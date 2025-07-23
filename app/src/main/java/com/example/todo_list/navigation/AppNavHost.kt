package com.example.todo_list.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo_list.ui.TaskDetailScreen
import com.example.todo_list.ui.TaskListScreen
import android.util.Log
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.todo_list.R
import com.example.todo_list.ui.ArchiveScreen
import com.example.todo_list.ui.SearchScreen
import com.example.todo_list.util.Logger
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val layoutDirection = LocalLayoutDirection.current
    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("BottomNavigation")
            ) {
                val items = listOf(
                    NavItem("Home", "task_list", R.drawable.ic_home),
                    NavItem("Search", "search", R.drawable.ic_search),
                    NavItem("Archive", "archive", R.drawable.ic_archive)
                )
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(item.iconRes), contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                            Logger.d("Navigated to ${item.route}")
                        },
                        modifier = Modifier.testTag("NavItem_${item.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "task_list",
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(layoutDirection),
                end = innerPadding.calculateEndPadding(layoutDirection)
            )
        ) {
            composable("task_list") {
                TaskListScreen(
                    onTaskClick = { taskId ->
                        navController.navigate("task_detail/$taskId")
                        Log.d("LOG_MSG", "Navigating to task_detail with ID: $taskId")
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
            composable("search") {
                SearchScreen()
            }
            composable("archive") {
                ArchiveScreen()
            }
        }
    }
}

data class NavItem(val title: String, val route: String, val iconRes: Int)