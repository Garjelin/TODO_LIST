package com.example.todo_list.data.repository

import com.example.todo_list.data.model.Task
import com.example.todo_list.data.model.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(title: String) {
        taskDao.insertTask(Task(title = title, isCompleted = false))
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
}