package com.nvko.todolist.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nvko.todolist.database.task.Task
import com.nvko.todolist.database.task.TaskDatabase
import com.nvko.todolist.database.task.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// viewmodel provides data to the UI and survive configuration changes
// it acts as a communication center between the repository and the ui
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val getAllTasks: LiveData<MutableList<Task>>
    private val taskRepository: TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        getAllTasks = taskRepository.getAllTasks
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) { // dispatchers.io means that i want to run this code as a background thread
            taskRepository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteTask(task)
        }
    }
}