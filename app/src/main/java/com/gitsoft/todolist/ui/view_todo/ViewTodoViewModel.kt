package com.gitsoft.todolist.ui.view_todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.todolist.model.Todo
import com.gitsoft.todolist.repository.local.TodoRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class ViewTodoViewModel(
    val repository: TodoRepository,
    todo: Todo,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _selectedTodo = MutableLiveData<Todo>()
    private val selectedTodo: LiveData<Todo> = _selectedTodo


    private val _todoData = MutableLiveData<Todo>()
    val todoData: LiveData<Todo> = _todoData

    private val _confirmDeleteSnackBar = MutableLiveData<Boolean>()
    val confirmDeleteSnackBar: LiveData<Boolean> = _confirmDeleteSnackBar


    private val _navigateToDisplay = MutableLiveData<Boolean>()
    val navigateToDisplay: LiveData<Boolean> = _navigateToDisplay


    private val _emptyTaskCheck = MutableLiveData<Boolean>()
    val emptyTaskCheck: LiveData<Boolean> = _emptyTaskCheck


    val todoTitle = MutableLiveData<String>()

    val todoDescription = MutableLiveData<String>()

    init {
        _selectedTodo.value = todo
        todoTitle.value = _selectedTodo.value!!.title!!
        todoDescription.value = selectedTodo.value!!.description!!
    }


    fun saveEditedTodo() {
        coroutineScope.launch {
            val id = _selectedTodo.value!!.todoId
            val title = todoTitle.value
            val description = todoDescription.value
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            val addedAt = sdf.format(Date())
            val isComplete = false

            if (title.isNullOrEmpty() && description.isNullOrEmpty()) {
                _emptyTaskCheck.value = true
                _navigateToDisplay.value = false
            } else {

                val todo = Todo(id, title, description, addedAt, isComplete)
                saveTodo(todo)

                _navigateToDisplay.value = true
            }
        }
    }
    
    
    private fun loadTodo() {
        coroutineScope.launch {

        }
    }

    fun finishedNavigatingToDisplay() {
        _navigateToDisplay.value = false
    }

    fun deleteTask(todo: Todo) {
        coroutineScope.launch {
            repository.delete(todo)

            _navigateToDisplay.value = true
        }
    }

    fun deleteTaskConfirmed() {
        _confirmDeleteSnackBar.value = true
    }

    fun finishConfirmDeleteTask() {
        _confirmDeleteSnackBar.value = false
    }

    fun deleteNotConfirmed() {

    }


    private suspend fun saveTodo(todo: Todo) {
        return withContext(Dispatchers.IO) {
            repository.update(todo)
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}