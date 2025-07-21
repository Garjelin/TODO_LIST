package com.example.todo_list.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list.util.Logger
import com.example.todo_list.viewModel.TaskViewModel

@Composable
fun SearchScreen() {
    val viewModel: TaskViewModel = viewModel()
    var searchQuery by remember { mutableStateOf("") }
    val tasks by viewModel.searchTasks(searchQuery).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("SearchScreen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                Logger.d("Search query: $it")
            },
            label = { Text("Search tasks") },
            modifier = Modifier.fillMaxWidth().testTag("SearchInput")
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize().testTag("SearchResults"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Text(
                    text = task.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .testTag("SearchTask_${task.id}")
                )
                Logger.d("Rendered search task: ${task.title}")
            }
        }
    }
}