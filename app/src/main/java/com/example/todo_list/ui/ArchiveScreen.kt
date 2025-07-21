package com.example.todo_list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list.viewModel.TaskViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.todo_list.util.Logger

@Composable
fun ArchiveScreen() {
    val viewModel: TaskViewModel = viewModel()
    val tasks by viewModel.getCompletedTasks().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("ArchiveScreen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Completed Tasks",
            modifier = Modifier.testTag("ArchiveTitle")
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("ArchiveList"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Text(
                    text = task.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("ArchiveTask_${task.id}")
                )
                Logger.d("Rendered archive task: ${task.title}")
            }
        }
    }
}