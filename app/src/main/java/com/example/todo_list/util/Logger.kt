package com.example.todo_list.util

import android.util.Log

object Logger {
    private const val TAG = "ToDoList"

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun e(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}