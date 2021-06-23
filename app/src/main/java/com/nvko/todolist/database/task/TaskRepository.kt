package com.nvko.todolist.database.task

import androidx.lifecycle.LiveData

// class that abstracts the access to multiple data sources
class TaskRepository(private val taskDao: TaskDao) {

    val getAllTasks: LiveData<MutableList<Task>> = taskDao.getAllTasks()

    fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }


}