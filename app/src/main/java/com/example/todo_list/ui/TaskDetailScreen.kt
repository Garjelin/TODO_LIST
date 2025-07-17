package com.example.todo_list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list.util.Logger
import com.example.todo_list.viewModel.TaskViewModel

@Composable
fun TaskDetailScreen(taskId: Int, onBackClick: () -> Unit) {
    val viewModel: TaskViewModel = viewModel()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())
    val task = tasks.find { it.id == taskId }

    if (task == null) {
        Text(
            text = "Task not found",
            modifier = Modifier.testTag("TaskNotFound")
        )
        return
    }

    val titleState = remember { mutableStateOf(task.title) }
    val isCompletedState = remember { mutableStateOf(task.isCompleted) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("TaskDetailScreen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task Details",
            modifier = Modifier.testTag("TaskDetailTitle")
        )
        TextField(
            value = titleState.value,
            onValueChange = { newValue ->
                titleState.value = newValue
                Logger.d("Task title changed: $newValue")
            },
            label = { Text("Task title") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("TaskTitleInput_${task.id}")
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompletedState.value,
                onCheckedChange = { newValue ->
                    isCompletedState.value = newValue
                    Logger.d("Task completed changed: $newValue")
                },
                modifier = Modifier.testTag("TaskCompletedCheckbox")
            )
            Text(
                text = "Completed",
                modifier = Modifier.testTag("TaskCompletedLabel")
            )
        }
        Button(
            onClick = {
                viewModel.updateTask(
                    task.copy(
                        title = titleState.value,
                        isCompleted = isCompletedState.value
                    )
                )
                Logger.d("Task saved: $titleState.value")
            },
            modifier = Modifier.testTag("SaveTaskButton")
        ) {
            Text("Save")
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.testTag("BackButton")
        ) {
            Text("Back")
        }
        Logger.d("Showing task: ${task.title} with ID: ${task.id}")
    }
}