package com.nvko.todolist.database.task

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM user_tasks ORDER BY due_date ASC")
    fun getAllTasks(): LiveData<MutableList<Task>>

}