package com.example.todo_list.viewModel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todo_list.data.model.AppDatabase
import com.example.todo_list.data.model.Task
import com.example.todo_list.data.repository.TaskRepository
import com.example.todo_list.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "task_filter_prefs")

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    private val dataStore = application.dataStore
    val tasks: Flow<List<Task>>
    private val showCompletedKey = booleanPreferencesKey("show_completed")
    val showCompletedFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[showCompletedKey] ?: true // Значение по умолчанию - true (Show All)
        }
    init {
        val taskDao = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "todo_database"
        ).build().taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.tasks
    }

    fun addTask(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(title)
            Logger.d("Task added: $title")
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
            Logger.d("Task deleted: ${task.title}")
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
            Logger.d("Task updated: ${task.title}")
        }
    }

    fun searchTasks(query: String): Flow<List<Task>> {
        return tasks.map { taskList ->
            taskList.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    fun getCompletedTasks(): Flow<List<Task>> {
        return tasks.map { taskList ->
            taskList.filter { it.isCompleted }
        }
    }

    fun getFilteredTasks(showCompleted: Boolean): Flow<List<Task>> {
        return tasks.map { taskList ->
            if (showCompleted) {
                taskList
            } else {
                taskList.filter { !it.isCompleted }
            }
        }
    }

    fun saveFilterState(showCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[showCompletedKey] = showCompleted
                Logger.d("Saved filter state: $showCompleted")
            }
        }
    }
}