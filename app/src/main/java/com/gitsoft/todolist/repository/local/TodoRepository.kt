package com.gitsoft.todolist.repository.local

import androidx.lifecycle.LiveData
import com.gitsoft.todolist.database.TodoDao
import com.gitsoft.todolist.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoRepository(private val todoDao: TodoDao) {


    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    fun getActiveTodos(isComplete: Boolean): LiveData<List<Todo?>> {
        return todoDao.getActiveTodos(isComplete)
    }

    suspend fun getCompletedTodos(isComplete: Boolean): LiveData<List<Todo?>> {
        return todoDao.getCompletedTodos(isComplete)
    }

    suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }


    suspend fun delete(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    suspend fun getOneTodo(): Todo? {
        return withContext(Dispatchers.IO) {
            todoDao.getOneTodo()
        }
    }

    suspend fun markCompleted(todo: Todo) {
        todoDao.setCompleted(todo.todoId.toString(), true)
    }


    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

}