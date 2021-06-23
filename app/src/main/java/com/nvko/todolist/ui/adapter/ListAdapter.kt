package com.nvko.todolist.ui.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nvko.todolist.database.task.Priority
import com.nvko.todolist.database.task.Task
import com.nvko.todolist.databinding.TaskItemBinding
import com.nvko.todolist.ui.fragment.ListFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

class ListAdapter(val onCheckboxClicked: (Task) -> Unit) : RecyclerView.Adapter<ListAdapter.TaskViewHolder>() {

    private var tasks: MutableList<Task> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.taskDoneCheckbox.isChecked = task.isDone
        holder.binding.taskTitleTextView.text = task.title

        val simpleDateFormat = SimpleDateFormat("EEEE, dd.MM.yyyy", Locale.getDefault())
        holder.binding.taskDueDateTextView.text = simpleDateFormat.format(task.dueDate)

        holder.binding.taskPriorityTextView.text = task.priority.name
        managePriorityTextColor(holder, task)

        setupCheckboxLogic(holder, task.isDone)
        holder.binding.taskDoneCheckbox.setOnCheckedChangeListener { _, _ ->
            setupCheckboxLogic(holder, !task.isDone)
            onCheckboxClicked(task)
        }

        holder.binding.taskItemRootLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToEditFragment(task)
            holder.itemView.findNavController().navigate(action)
        }
    }

    private fun setupCheckboxLogic(holder: TaskViewHolder, isDone: Boolean) {
        if (isDone) {
            holder.binding.taskDoneCheckbox.isChecked = true
            holder.binding.taskTitleTextView.paintFlags =
                holder.binding.taskTitleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.taskDoneCheckbox.isChecked = false
            holder.binding.taskTitleTextView.paintFlags =
                holder.binding.taskTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    private fun managePriorityTextColor(holder: TaskViewHolder, task: Task) {
        when (task.priority) {
            Priority.Low -> holder.binding.taskPriorityTextView.setTextColor(Color.GREEN)
            Priority.Medium -> holder.binding.taskPriorityTextView.setTextColor(Color.rgb(200, 200, 0))
            Priority.High -> holder.binding.taskPriorityTextView.setTextColor(Color.rgb(255, 100, 0))
            Priority.ASAP -> holder.binding.taskPriorityTextView.setTextColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun getItemAtPosition(position: Int): Task {
        return tasks[position]
    }

    fun setTasks(tasks: MutableList<Task>) {
        this.tasks = tasks;
        notifyDataSetChanged()
    }

    fun removeAt(adapterPosition: Int) {
        tasks.removeAt(adapterPosition)
        notifyDataSetChanged()
    }

    class TaskViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)
}
