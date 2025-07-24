package com.example.todo_list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
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
            .testTag("TaskDetailScreen"),
    ) {
        Surface(
            color = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.testTag("BackIcon")
                    )
                }
                Text(
                    text = "Task Details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("TaskDetailHeader")
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("SaveTaskButton")
            ) {
                Text("Save")
            }
        }
        Logger.d("Showing task: ${task.title} with ID: ${task.id}")
    }
}