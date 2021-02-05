package com.example.todojosiasassamoi.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todojosiasassamoi.R
import com.example.todojosiasassamoi.databinding.FragmentTaskListBinding
import com.example.todojosiasassamoi.databinding.ItemTaskBinding
import com.example.todojosiasassamoi.task.Task
class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback)

{
    private var _binding: ItemTaskBinding? = null
    private val binding get() = _binding!!

    // Déclaration de la variable lambda dans l'adapter:
    var onDeleteTask: ((Task) -> Unit)? = null
    var onEditTask: ((Task) -> Unit)? = null

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(task: Task) {

            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                // TODO: afficher les données et attacher les listeners aux différentes vues de notre [itemView]
                val delButton = findViewById<ImageButton>(R.id.deleteTaskButton)
                delButton.setOnClickListener {
                    onDeleteTask?.invoke(task)
                }
                val editButton = binding.editTaskButton

                editButton.setOnClickListener {
                    onEditTask?.invoke(task)
                }

                val tView =binding.taskTitle
                tView.text = task.title + "\nDescription : " + task.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        _binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         val itemView = binding.root
        return TaskViewHolder(itemView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _binding=null
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    object TasksDiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id== newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    }
}
