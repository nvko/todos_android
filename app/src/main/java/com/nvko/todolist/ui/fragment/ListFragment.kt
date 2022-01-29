package com.nvko.todolist.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nvko.todolist.R
import com.nvko.todolist.database.task.Task
import com.nvko.todolist.ui.viewmodel.TaskViewModel
import com.nvko.todolist.databinding.FragmentListBinding
import com.nvko.todolist.ui.utils.SwipeToDeleteCallback
import com.nvko.todolist.ui.adapter.ListAdapter
import com.nvko.todolist.ui.utils.onQueryTextChanged

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        setHasOptionsMenu(true)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val listAdapter = ListAdapter(::onCheckboxClick)
        val linearLayoutManager = LinearLayoutManager(context)
        binding.taskListRecyclerView.layoutManager = linearLayoutManager
        binding.taskListRecyclerView.adapter = listAdapter
        binding.taskListRecyclerView.setHasFixedSize(true)
        val dividerItemDecoration = DividerItemDecoration(
            binding.taskListRecyclerView.context,
            linearLayoutManager.orientation
        )
        binding.taskListRecyclerView.addItemDecoration(dividerItemDecoration)
        taskViewModel.getAllTasks.observe(
            viewLifecycleOwner,
            { tasks -> listAdapter.setTasks(tasks.toMutableList()) })
        setupSwipeToDeleteFunction(listAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search_tasks);
        val searchView = searchItem.actionView as SearchView
        searchView.onQueryTextChanged {
            taskViewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
         when(item.itemId) {
            R.id.action_search_tasks -> {
                return true
            }
            R.id.action_sort_by_date -> {
                taskViewModel.sortOrder.value = TaskViewModel.SortOrder.BY_DATE
                return true
            }
            R.id.action_sort_by_title -> {
                taskViewModel.sortOrder.value = TaskViewModel.SortOrder.BY_TITLE
                return true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                taskViewModel.hideCompleted.value = item.isChecked
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private fun setupSwipeToDeleteFunction(listAdapter: ListAdapter) {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                taskViewModel.deleteTask(listAdapter.getItemAtPosition(viewHolder.adapterPosition))
                listAdapter.removeAt(viewHolder.adapterPosition)
                Toast.makeText(context, "Task has been deleted!", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.taskListRecyclerView)
    }

    private fun onCheckboxClick(task: Task) {
        task.isDone = !task.isDone
        taskViewModel.updateTask(task)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}