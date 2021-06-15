package com.gitsoft.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gitsoft.todolist.model.Priority
import com.gitsoft.todolist.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Query("select * from todo_table where isComplete = 0 order by todoId desc")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("select * from todo_table where isComplete like :isComplete order by addedAt desc")
    fun getCompletedTodos(isComplete: Boolean): LiveData<List<Todo?>>

    @Query("select * from todo_table where isComplete like :isActive order by addedAt")
    fun getActiveTodos(isActive: Boolean): LiveData<List<Todo?>>

    @Query("select * from todo_table where isComplete = 0 order by addedAt desc limit 1")
    fun getOneTodo(): Todo?

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("update todo_table set isComplete = :isComplete where todoId = :todoId")
    suspend fun setCompleted(todoId: String, isComplete: Boolean)

    @Query("delete from todo_table")
    suspend fun deleteAll()

}