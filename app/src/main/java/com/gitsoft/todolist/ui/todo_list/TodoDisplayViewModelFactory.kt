package com.gitsoft.todolist.ui.todo_list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.todolist.repository.local.TodoRepository
import java.lang.IllegalArgumentException

class TodoDisplayViewModelFactory(
    private val repository: TodoRepository,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(TodoDisplayViewModel::class.java)) {
            return TodoDisplayViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}