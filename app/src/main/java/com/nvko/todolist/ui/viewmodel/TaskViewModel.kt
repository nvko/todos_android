package com.nvko.todolist.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nvko.todolist.database.task.Task
import com.nvko.todolist.database.task.TaskDatabase
import com.nvko.todolist.database.task.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

// viewmodel provides data to the UI and survive configuration changes
// it acts as a communication center between the repository and the ui
class TaskViewModel(application: Application) : AndroidViewModel(application) {

    val taskDao = TaskDatabase.getDatabase(application).taskDao()

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
    val hideCompleted = MutableStateFlow(false)

    private val tasksFlow =
        combine(searchQuery, sortOrder, hideCompleted) { query, sortOrder, hideCompleted ->
            Triple(query, sortOrder, hideCompleted)
        }.flatMapLatest { taskDao.getTasks(it.first, it.second, it.third) }

    private val taskRepository = TaskRepository(taskDao)
    val getAllTasks = tasksFlow.asLiveData()


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

    enum class SortOrder { BY_TITLE, BY_DATE }
}