package com.gitsoft.todolist.ui.add_todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gitsoft.todolist.model.Priority
import com.gitsoft.todolist.model.Todo
import com.gitsoft.todolist.repository.local.TodoRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class AddTodoViewModel(
    private val repository: TodoRepository,
    application: Application
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _navigateToDisplayFragment = MutableLiveData<Boolean>()
    val navigateToDisplayFragment: LiveData<Boolean>
        get() = _navigateToDisplayFragment

    private val _showEmptySnackBarEvent = MutableLiveData<Boolean>()
    val showEmptySnackBarEvent: LiveData<Boolean>
        get() = _showEmptySnackBarEvent

    private val _showNoteAddedSnackBarEvent = MutableLiveData<Boolean>()
    val showNoteAddedSnackBarEvent: LiveData<Boolean>
        get() = _showNoteAddedSnackBarEvent


    private val todoData = MutableLiveData<Todo?>()

    val allTodos: LiveData<List<Todo>> = repository.allTodos


    val title = MutableLiveData<String>()


    val priority = MutableLiveData<Priority?>()


    val description = MutableLiveData<String>()

    init {
        initializeTodoDatabase()
    }

    private fun initializeTodoDatabase() {
        coroutineScope.launch {
            todoData.value = getOneTodo()
        }
    }

    private suspend fun getOneTodo(): Todo? {
        return withContext(Dispatchers.IO) {
            repository.getOneTodo()
        }
    }

    fun onSaveTodo() {
        coroutineScope.launch {


            if (title.value.isNullOrEmpty() && description.value.isNullOrEmpty()
            ) {
                _showEmptySnackBarEvent.value = true
                _navigateToDisplayFragment.value = true
            } else if (title.value?.isNotEmpty() == true && description.value.isNullOrEmpty()) {
                val todoTitle = title.value
                val description = ""
                val time = SimpleDateFormat("h:mm a", Locale("English"))
                val currentTime = time.format(Date()).toString()
                val isComplete = false
                val todoInfo =
                    Todo(0, todoTitle, description, currentTime, isComplete)
                insert(todoInfo)

                todoData.value = getOneTodo()
                _showNoteAddedSnackBarEvent.value = true
                _navigateToDisplayFragment.value = true

            } else if (title.value.isNullOrEmpty() && description.value?.isNotEmpty() == true) {
                val todoTitle = ""
                val description = description.value
                val timeFormat = SimpleDateFormat("h:mm a", Locale("English"))
                val currentTime = timeFormat.format(Date()).toString()
                val isComplete = false
                val todoInfo =
                    Todo(0, todoTitle, description, currentTime, isComplete)
                insert(todoInfo)




                todoData.value = getOneTodo()
                _showNoteAddedSnackBarEvent.value = true
                _navigateToDisplayFragment.value = true
            } else {
                val todoTitle = title.value
                val description = description.value
                val time = SimpleDateFormat("h:mm a", Locale.getDefault())
                val isComplete = false
                val currentTime = time.format(Date())
                val todoInfo =
                    Todo(0, todoTitle.toString(), description.toString(), currentTime, isComplete)
                insert(todoInfo)

                todoData.value = getOneTodo()
                _showNoteAddedSnackBarEvent.value = true
                _navigateToDisplayFragment.value = true
            }


        }
    }

    private suspend fun insert(todo: Todo) {
        return withContext(Dispatchers.IO) {
            repository.insert(todo)
        }
    }

    fun onDoneShowingEmptySnackBar() {
        _showEmptySnackBarEvent.value = false
    }

    fun onDoneNavigatingToDisplay() {
        _navigateToDisplayFragment.value = false
    }

    fun onDoneShowingNoteAddedSnackBar() {
        _showNoteAddedSnackBarEvent.value = false
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

}