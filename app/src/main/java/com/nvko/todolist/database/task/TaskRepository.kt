package com.nvko.todolist.database.task

import kotlinx.coroutines.flow.Flow

// class that abstracts the access to multiple data sources
class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(searchQuery: String, hideCompleted: Boolean) : Flow<MutableList<Task>> {
        return taskDao.getAllTasksByDate(searchQuery, hideCompleted);
    }

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