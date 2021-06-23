package com.nvko.todolist.ui.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.nvko.todolist.R
import com.nvko.todolist.database.task.Priority
import java.util.stream.Collectors
import java.util.stream.Stream

class AddEditUtils {

    companion object {
        fun setupAdapter(
            context: Context,
            priorityAutoCompleteTextView: AutoCompleteTextView
        ): ArrayAdapter<String> {
            val priorityNames = Stream.of(*Priority.values()).map { obj: Priority -> obj.name }
                .collect(Collectors.toList())
            val arrayAdapter = ArrayAdapter(context, R.layout.priority_item, priorityNames)
            priorityAutoCompleteTextView.setText(arrayAdapter.getItem(1))
            priorityAutoCompleteTextView.setAdapter(arrayAdapter)
            return arrayAdapter
        }
    }
}