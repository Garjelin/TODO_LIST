package com.example.todo_list.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.todo_list.data.model.AppDatabase
import com.example.todo_list.data.model.Task
import com.example.todo_list.data.model.TaskDao
import com.example.todo_list.data.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
            Log.d("LOG_MSG", "Task updated: ${task.title}")
        }
    }
}