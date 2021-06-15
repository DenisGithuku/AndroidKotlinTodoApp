package com.gitsoft.todolist.ui.todo_list

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.gitsoft.todolist.R
import com.gitsoft.todolist.database.TodoDatabase
import com.gitsoft.todolist.repository.local.TodoRepository
import com.gitsoft.todolist.ui.todo_list.TodoDisplayFragmentDirections
import com.gitsoft.todolist.utils.TodoAdapter

class TodoDisplayFragment : Fragment() {
    private lateinit var binding: TodDisplayFragmentBinding
    private lateinit var viewModel: TodoDisplayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TodoDisplayFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val application = requireNotNull(this.activity).application
        val database = TodoDatabase.getDatabaseInstance(application).todoDao
        val repository = TodoRepository(database)
        val viewModelFactory =
            com.gitsoft.todolist.ui.todo_list.TodoDisplayViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoDisplayViewModel::class.java)
        binding.viewModel = viewModel
        val adapter = TodoAdapter(viewModel)

        viewModel.navigateToViewTodo.observe(viewLifecycleOwner, {
            if (it != null) {
                val navController = findNavController()
                navController.navigate(
                    TodoDisplayFragmentDirections.actionTodoDisplayFragmentToViewTodoFragment(
                        it
                    )
                )
                viewModel.onFinishedNavigatingToViewTodo()
            }
        })
        binding.listItem.adapter = adapter
        viewModel.navigateToNewTodo.observe(viewLifecycleOwner, {
            if (it == true) {
                val navController = findNavController()
                navController.navigate(TodoDisplayFragmentDirections.actionTodoDisplayFragmentToAddTodoFragment())
                viewModel.onDoneNavigatingToNewTodo()
            }
        })

        viewModel.toastEvent.observe(viewLifecycleOwner, { event ->
            if (true == event) {
                Toast.makeText(
                    requireContext(),
                    "Replace with your own action",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.finishedShowingToast()
            }
        })
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_display_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.mark_all -> {
            viewModel.onDeleteAllTodos()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}