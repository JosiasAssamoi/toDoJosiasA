package com.example.todojosiasassamoi.tasklist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todojosiasassamoi.MainActivity
import com.example.todojosiasassamoi.R
import com.example.todojosiasassamoi.databinding.ActivityTaskBinding
import com.example.todojosiasassamoi.databinding.FragmentTaskListBinding
import com.example.todojosiasassamoi.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback)


{


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
                val editButton = findViewById<ImageButton>(R.id.editTaskButton)

                editButton.setOnClickListener {
                    onEditTask?.invoke(task)
                }

                val tView = findViewById<TextView>(R.id.task_title)
                tView.text = task.title + "\nDescription : " + task.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)

        return TaskViewHolder(itemView)
    }
    private var _binding: FragmentTaskListBinding? = null

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    object TasksDiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id== newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    }
}
