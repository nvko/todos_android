package com.nvko.todolist.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.CalendarView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.nvko.todolist.R
import com.nvko.todolist.database.task.Priority
import com.nvko.todolist.database.task.Task
import com.nvko.todolist.ui.viewmodel.TaskViewModel
import com.nvko.todolist.databinding.FragmentAddBinding
import com.nvko.todolist.ui.utils.AddEditUtils
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task

    private lateinit var titleEditText: TextInputEditText
    private lateinit var calendarView: CalendarView
    private lateinit var priorityAutoCompleteTextView: AutoCompleteTextView
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        titleEditText = binding.taskTitleEditText
        calendarView = binding.taskDueDateCalendarView
        priorityAutoCompleteTextView = binding.priorityAutoCompleteTextView

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        task = Task()

        titleEditText.doOnTextChanged { text, _, _, _ -> task.title = text.toString() }
        calendarView.setOnDateChangeListener { calendarView, _, _, _ ->
            task.dueDate = Date(calendarView.date)
        }
        setupPriorityMenu()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_task_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.action_save_new_task) {
            addTask()
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupPriorityMenu() {
        val arrayAdapter = AddEditUtils.setupAdapter(requireContext(), priorityAutoCompleteTextView)
        priorityAutoCompleteTextView.setOnItemClickListener { adapterView, view, pos: Int, _ ->
            task.priority = Priority.valueOf(arrayAdapter.getItem(pos)!!)
        }
    }

    private fun addTask() {
        if (validateFields(task.title))
            taskViewModel.addTask(task)
        else
            Toast.makeText(context, "Title should not be empty!", Toast.LENGTH_SHORT).show();
    }

    private fun validateFields(title: String): Boolean {
        return title.isNotEmpty();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}