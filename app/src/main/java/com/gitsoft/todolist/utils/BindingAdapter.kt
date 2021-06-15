package com.gitsoft.todolist.utils

import android.graphics.Paint
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.recyclerview.widget.RecyclerView
import com.gitsoft.todolist.database.TodoDao
import com.gitsoft.todolist.model.Todo
import com.gitsoft.todolist.repository.local.TodoRepository
import com.gitsoft.todolist.ui.todo_list.TodoState


@BindingAdapter("todos")
fun bindTodos(recyclerView: RecyclerView, data: List<Todo>?) {
    val adapter = recyclerView.adapter as TodoAdapter
    adapter.submitList(data)
}

@BindingAdapter("app:completed")
fun checkVisibility(layout: LinearLayout, completed: Boolean) {
    if (completed) {
        layout.visibility = View.GONE
    } else {
        layout.visibility = View.VISIBLE
    }
}



@BindingAdapter("app:text_through")
fun textAppearance(textView: TextView, isComplete: Boolean) {
    if (isComplete) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

@BindingAdapter("app:todo_state")
fun checkStatus(textView: TextView, todos: List<Todo>) {
        if (todos.isEmpty()) {
            textView.text = "Add new tasks"
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
}
