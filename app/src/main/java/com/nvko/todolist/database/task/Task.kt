package com.nvko.todolist.database.task

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user_tasks")
data class Task(

    @PrimaryKey(autoGenerate = true) val id: Int?,
    var title: String = "",
    @ColumnInfo(name = "due_date") var dueDate: Date = Date(),
    @ColumnInfo(name = "is_done") var isDone: Boolean = false,
    @ColumnInfo(name = "priority") var priority: Priority = Priority.Medium

): Parcelable {
    constructor() : this(null, "", Date(), false, Priority.Medium)
}