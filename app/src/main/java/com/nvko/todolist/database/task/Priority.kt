package com.nvko.todolist.database.task

enum class Priority(importance: Int) {
    Low(0), Medium(1), High(2), ASAP(3);

    var importance: Int = importance

    fun isMoreImportantThan(otherPriority: Priority): Int {
        return importance.compareTo(otherPriority.importance)
    }

}