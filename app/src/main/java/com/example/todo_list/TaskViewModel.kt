package com.example.todo_list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao: TaskDao = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "todo_database"
    ).build().taskDao()

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun addTask(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insertTask(Task(title = title, isCompleted = false))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.updateTask(task)
            Log.d("LOG_MSG", "Task updated: ${task.title}")
        }
    }
}