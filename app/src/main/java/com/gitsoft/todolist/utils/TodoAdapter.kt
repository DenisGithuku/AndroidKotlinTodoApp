package com.gitsoft.todolist.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gitsoft.todolist.databinding.TodoItemBinding
import com.gitsoft.todolist.model.Todo
import com.gitsoft.todolist.ui.todo_list.TodoDisplayViewModel

class TodoAdapter(private val viewModel: TodoDisplayViewModel) : ListAdapter<Todo, TodoAdapter.ViewHolder>(DiffCallBack) {
    class ViewHolder(private val binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo, viewModel: TodoDisplayViewModel) {
            binding.todoData = todo
            binding.todoDisplayViewModel = viewModel
            binding.executePendingBindings()
        }

    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.todoId == newItem.todoId
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TodoItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(position)
        holder.bind(todo, viewModel)
    }
}