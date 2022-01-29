package com.nvko.todolist.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nvko.todolist.R
import com.nvko.todolist.database.task.Priority
import com.nvko.todolist.databinding.FragmentEditBinding
import com.nvko.todolist.databinding.FragmentListBinding
import com.nvko.todolist.ui.utils.AddEditUtils
import com.nvko.todolist.ui.viewmodel.TaskViewModel
import java.util.*

class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel
    private val args: EditFragmentArgs by navArgs()
    private var isEdited: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.updateTaskTitleEditText.setText(args.currentTask.title)
        binding.updatePriorityAutoCompleteTextView.setText(args.currentTask.priority.name)
        binding.updateTaskDueDateCalendarView.date = args.currentTask.dueDate.time

        setHasOptionsMenu(true)
        setupPriorityMenu()

        binding.updateTaskTitleEditText.doOnTextChanged { text, _, _, _ ->
            args.currentTask.title = text.toString()
            if (!isEdited) isEdited = true
        }

        binding.updateTaskDueDateCalendarView.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth);
            args.currentTask.dueDate = calendar.time
            if (!isEdited) isEdited = true
        }

        return binding.root
    }

    private fun setupPriorityMenu() {
        val arrayAdapter =
            AddEditUtils.setupAdapter(requireContext(), binding.updatePriorityAutoCompleteTextView)
        binding.updatePriorityAutoCompleteTextView.setOnItemClickListener { _, _, pos: Int, _ ->
            args.currentTask.priority = Priority.valueOf(arrayAdapter.getItem(pos)!!)
            if (!isEdited) isEdited = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_task_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.action_save_new_task) {
            if (isEdited) {
                if (updateTask())
                    findNavController().popBackStack()
            } else {
                findNavController().popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTask(): Boolean {
        if (args.currentTask.title.isNotEmpty()) {
            taskViewModel.updateTask(args.currentTask)
            return true
        } else {
            Toast.makeText(requireContext(), "Title should not be empty!", Toast.LENGTH_SHORT)
                .show()
            return false
        }
    }


}