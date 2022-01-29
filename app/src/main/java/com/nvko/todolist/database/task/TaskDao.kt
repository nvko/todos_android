package com.nvko.todolist.database.task

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM user_tasks WHERE title LIKE '%' || :searchQuery || '%' ORDER BY due_date ASC")
    fun getAllTasks(searchQuery: String): Flow<MutableList<Task>>

}