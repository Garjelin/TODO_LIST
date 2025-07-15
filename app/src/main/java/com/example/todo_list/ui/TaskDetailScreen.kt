package com.example.todo_list.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list.viewModel.TaskViewModel

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