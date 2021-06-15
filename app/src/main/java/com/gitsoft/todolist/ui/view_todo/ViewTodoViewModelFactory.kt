package com.gitsoft.todolist.ui.view_todo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gitsoft.todolist.model.Todo
import com.gitsoft.todolist.repository.local.TodoRepository

class ViewTodoViewModelFactory(
    private val repository: TodoRepository,
    private val todo: Todo,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(ViewTodoViewModel::class.java)) {
            return ViewTodoViewModel(repository, todo, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}