package com.gitsoft.todolist.ui.todo_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.todolist.model.Todo
import com.gitsoft.todolist.repository.local.TodoRepository
import kotlinx.coroutines.*

enum class TodoState {
    ALL,
    ACTIVE,
    COMPLETED
}

class TodoDisplayViewModel(
    private val repository: TodoRepository,
    application: Application
) : AndroidViewModel(application) {
    private val _navigateToNewTodo = MutableLiveData<Boolean>()
    val navigateToNewTodo: LiveData<Boolean>
        get() = _navigateToNewTodo

    private val _toastEvent = MutableLiveData<Boolean>()
    val toastEvent: LiveData<Boolean>
        get() = _toastEvent

    private val _navigateToViewTodo = MutableLiveData<Todo?>()
    val navigateToViewTodo: LiveData<Todo?>
        get() = _navigateToViewTodo

    val allTodos: LiveData<List<Todo>> = repository.allTodos


    private val _completedTodos = MutableLiveData<Todo?>()
    val completedTodos: LiveData<Todo?> = _completedTodos

    private val _todoState = MutableLiveData<TodoState?>()
    val todoState: LiveData<TodoState?> = _todoState


    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun onNavigateToNewTodo() {
        _navigateToNewTodo.value = true
    }

    fun onDoneNavigatingToNewTodo() {
        _navigateToNewTodo.value = false
    }

    fun onDeleteAllTodos() {
        coroutineScope.launch {
            repository.deleteAll()
        }
    }

    fun onClickMarkComplete(todo: Todo, isCompleted: Boolean) {
        coroutineScope.launch {
            if (isCompleted) {
                repository.markCompleted(todo)
            }
        }
    }


    fun onNavigateToViewTodo(todo: Todo) {
        _navigateToViewTodo.value = todo
    }

    fun onFinishedNavigatingToViewTodo() {
        _navigateToViewTodo.value = null
    }

    fun finishedShowingToast() {
        _toastEvent.value = false
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

}