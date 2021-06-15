package com.gitsoft.todolist.ui.view_todo

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import com.gitsoft.todolist.R
import com.gitsoft.todolist.database.TodoDatabase
import com.gitsoft.todolist.databinding.ViewTodoFragmentBinding
import com.gitsoft.todolist.repository.local.TodoRepository
import com.google.android.material.snackbar.Snackbar

class ViewTodoFragment : Fragment() {
    private lateinit var viewModel: ViewTodoViewModel
    private lateinit var binding: ViewTodoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewTodoFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val arguments = ViewTodoFragmentArgs.fromBundle(requireArguments()).todo
        val dao = TodoDatabase.getDatabaseInstance(application).todoDao
        val repository = TodoRepository(dao)
        val viewModelFactory = ViewTodoViewModelFactory(repository, arguments, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewTodoViewModel::class.java)
        binding.viewModel = viewModel


        viewModel.navigateToDisplay.observe(viewLifecycleOwner, { navigate ->
            if (navigate == true) {
                val navController = findNavController()
                navController.navigate(ViewTodoFragmentDirections.actionViewTodoFragmentToTodoDisplayFragment())
                viewModel.finishedNavigatingToDisplay()
            }
        })

        viewModel.emptyTaskCheck.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Title or description cannot be empty",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.confirmDeleteSnackBar.observe(viewLifecycleOwner, {
            if (it == true) {
                val snackBar = Snackbar.make(requireActivity().findViewById(android.R.id.content),
                "Confirm delete task",
                Snackbar.LENGTH_SHORT
                )

                snackBar.setAction("Okay") {
                    viewModel.deleteTask(arguments)
                }

                snackBar.show()

            }
        })


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.view_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_delete -> {
            viewModel.deleteTaskConfirmed()
            true
        }

        R.id.save_action -> {
            viewModel.saveEditedTodo()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}