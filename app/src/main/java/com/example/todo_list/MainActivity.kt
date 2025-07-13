package com.example.todo_list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list.ui.theme.TODO_LISTTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContent {
            TODO_LISTTheme {
                val navController = rememberNavController()
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
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
    }
}

@Composable
fun TaskListScreen(onTaskClick: (Int) -> Unit) {
    val viewModel: TaskViewModel = viewModel()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    var taskText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = taskText,
            onValueChange = {
                taskText = it
                Log.d("LOG_MSG", "Text changed: $it")
            },
            label = { Text("Enter new task") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (taskText.isNotEmpty()) {
                    Log.d("LOG_MSG", "Task added: $taskText")
                    viewModel.addTask(taskText)
                    taskText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Log.d("LOG_MSG", "Rendered task: $task")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onTaskClick(task.id) }
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = {
                            viewModel.updateTask(task.copy(isCompleted = it))
                            Log.d("LOG_MSG", "Task updated: ${task.title}, completed: $it")
                        }
                    )
                    Text(
                        text = task.title,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                    Button(onClick = {
                        viewModel.deleteTask(task)
                        Log.d("LOG_MSG", "Task deleted: $task")
                    }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
fun TaskDetailScreen(taskId: Int, onBackClick: () -> Unit) {
    val viewModel: TaskViewModel = viewModel()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val task = tasks.find { it.id == taskId }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (task != null) {
            Text(text = "Task Details: ${task.title}")
            Text(text = "Completed: ${task.isCompleted}")
            Log.d("TaskDetail", "Showing task: ${task.title}")
        } else {
            Text(text = "Task not found")
        }
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TODO_LISTTheme {
        Greeting("Android")
    }
}