package com.example.todo_list.ui

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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list.util.Logger
import com.example.todo_list.viewModel.TaskViewModel
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun TaskListScreen(onTaskClick: (Int) -> Unit) {
    val viewModel: TaskViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) }
    var showCompleted by remember { mutableStateOf(viewModel.showCompleted) } // Начальное значение из SharedPreferences
    val tasks by viewModel.getFilteredTasks(showCompleted).collectAsState(initial = emptyList())
    var taskText by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(if (showCompleted) 0 else 1) } // Индекс активного таба (0 - Hide, 1 - Show)

    LaunchedEffect(Unit) {
        isLoading = false // Сразу завершаем загрузку, так как данные доступны
        Logger.d("Initial filter state loaded from SharedPreferences: $showCompleted")
    }

    // Сохранение состояния при изменении
    LaunchedEffect(showCompleted) {
        viewModel.saveFilterState(showCompleted)
        Logger.d("Filter state synchronized: $showCompleted")
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .testTag("TaskListScreen")
    ) {
        TextField(
            value = taskText,
            onValueChange = {
                taskText = it
                Logger.d("Text changed: $it")
            },
            label = {
                Text(
                    text = "Enter new task",
                    modifier = Modifier.testTag("TaskInputLabel")
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("TaskInput")
        )
        Button(
            onClick = {
                if (taskText.isNotEmpty()) {
                    Logger.d("Task added: $taskText")
                    viewModel.addTask(taskText)
                    taskText = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("AddTaskButton")
        ) {
            Text("Add Task")
        }
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.testTag("FilterTabRow")
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    selectedTabIndex = 0
                    showCompleted = true // Показать все
                    Logger.d("Filter toggled to Show All")
                },
                text = { Text("Show All") },
                modifier = Modifier.testTag("ShowAllTab")
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    selectedTabIndex = 1
                    showCompleted = false // Скрыть выполненные
                    Logger.d("Filter toggled to Hide completed")
                },
                text = { Text("Hide completed") },
                modifier = Modifier.testTag("HideCompletedTab")
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("TaskList"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                Logger.d("Rendered task: ${task.title}")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onTaskClick(task.id) }
                        .testTag("TaskRow_${task.id}")
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = {
                            viewModel.updateTask(task.copy(isCompleted = it))
                            Logger.d("Task updated: ${task.title}, completed: $it")
                        },
                        modifier = Modifier.testTag("TaskCheckbox_${task.id}")
                    )
                    Text(
                        text = task.title,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .testTag("TaskTitle_${task.id}")
                    )
                    Button(
                        onClick = {
                            viewModel.deleteTask(task)
                            Logger.d("Task deleted: ${task.title}")
                        },
                        modifier = Modifier.testTag("DeleteTaskButton_${task.id}")
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}