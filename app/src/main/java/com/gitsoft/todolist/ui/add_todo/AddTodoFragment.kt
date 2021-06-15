package com.gitsoft.todolist.ui.add_todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gitsoft.todolist.database.TodoDatabase
import com.gitsoft.todolist.databinding.AddTodoFragmentBinding
import com.gitsoft.todolist.repository.local.TodoRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class AddTodoFragment : BottomSheetDialogFragment() {

    private lateinit var binding: AddTodoFragmentBinding
    private lateinit var viewModel: AddTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTodoFragmentBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val todoDao = TodoDatabase.getDatabaseInstance(application).todoDao
        val repository = TodoRepository(todoDao)
        val viewModelFactory = AddTodoViewModelFactory(repository, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddTodoViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.navigateToDisplayFragment.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController()
                    .navigate(AddTodoFragmentDirections.actionAddTodoFragmentToTodoDisplayFragment())
                viewModel.onDoneNavigatingToDisplay()
            }
        })

        viewModel.showEmptySnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Empty Todo Discarded",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.onDoneShowingEmptySnackBar()
            }
        })

        viewModel.showNoteAddedSnackBarEvent.observe(viewLifecycleOwner, {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Todo Added Successfully",
                    Snackbar.LENGTH_SHORT
                ).show()

                Log.i("todos", viewModel.allTodos.toString())

                viewModel.onDoneShowingNoteAddedSnackBar()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }
}