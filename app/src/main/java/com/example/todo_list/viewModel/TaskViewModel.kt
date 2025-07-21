package com.example.todo_list.viewModel

import android.app.Application
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

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val tasks: Flow<List<Task>>
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
}