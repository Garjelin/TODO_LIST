package com.example.todo_list.ui

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list.viewModel.TaskViewModel

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