package com.nvko.todolist.database.task

import androidx.room.*
import com.nvko.todolist.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(
        searchQuery: String,
        sortOrder: TaskViewModel.SortOrder,
        hideCompleted: Boolean
    ): Flow<List<Task>> {
        return if (TaskViewModel.SortOrder.BY_DATE == sortOrder) {
            getAllTasksByDate(searchQuery, hideCompleted)
        } else {
            getAllTasksByTitle(searchQuery, hideCompleted)
        }
    }


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM user_tasks WHERE (is_done != :hideCompleted OR is_done = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY due_date ASC")
    fun getAllTasksByDate(searchQuery: String, hideCompleted: Boolean): Flow<MutableList<Task>>

    @Query("SELECT * FROM user_tasks WHERE (is_done != :hideCompleted OR is_done = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY title ASC")
    fun getAllTasksByTitle(searchQuery: String, hideCompleted: Boolean): Flow<MutableList<Task>>

}