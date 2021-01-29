package com.example.todojosiasassamoi.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todojosiasassamoi.R
import com.example.todojosiasassamoi.databinding.ActivityTaskBinding
import com.example.todojosiasassamoi.tasklist.Task
import java.util.*

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    companion object {
        const val ADD_TASK_REQUEST_CODE = 777
        const  val EXTRA_REPLY = "task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        val taskView = binding.root
        setContentView(taskView)

        binding.textView.setText(intent?.getStringExtra("page")?.toString())
        binding.titleEditText.setText(intent?.getStringExtra("task_title")?.toString())
        binding.decriptionEditText.setText(intent?.getStringExtra("task_description")?.toString())

        binding.ValidateButton.setOnClickListener {
            val id = intent.getStringExtra("task_id") ?: UUID.randomUUID().toString()
            val newTask = Task(id = id, title = binding.titleEditText.text.toString(), description = binding.decriptionEditText.text.toString())
            intent.putExtra(EXTRA_REPLY, newTask)


            setResult(RESULT_OK, intent)
            finish()
        }

    }
}
