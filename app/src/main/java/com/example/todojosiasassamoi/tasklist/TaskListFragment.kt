package com.example.todojosiasassamoi.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.todojosiasassamoi.databinding.FragmentTaskListBinding
import com.example.todojosiasassamoi.network.Api
import com.example.todojosiasassamoi.task.TaskActivity
import kotlinx.coroutines.launch
import com.example.todojosiasassamoi.models.TaskListViewModel
import com.example.todojosiasassamoi.models.UserInfoViewModel
import com.example.todojosiasassamoi.task.Task
import com.example.todojosiasassamoi.userInfo.UserInfoActivity

class TaskListFragment : Fragment() {

    val myAdapter = TaskListAdapter()
    val userviewModel: UserInfoViewModel by viewModels()

    // On récupère une instance de ViewModel
    private val viewModel: TaskListViewModel by viewModels()
    //binding
    private val binding get() = _binding!!
    //clé du putextra pour le choix de la prochaine activity
    private val putExtraPageKey = "page"



    // lambda qui gere le retour d'une activity => pour la tache 
    val startForTaskResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = result.data!!.getSerializableExtra(TaskActivity.EXTRA_REPLY) as Task
                val index = viewModel.taskList.value?.indexOfFirst { it.id == task.id }
                if (index != -1) {
                    lifecycleScope.launch {
                        viewModel.editTask(task)
                    }
                } else {
                    lifecycleScope.launch {
                        viewModel.addTask(task)
                    }
                }
            }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private var _binding: FragmentTaskListBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        userviewModel.loadInfo()
        viewModel.loadTasks()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadTasks()
        viewModel.taskList.observe(viewLifecycleOwner) { list ->
            myAdapter.submitList(list)
        }

        userviewModel.userInfos.observe(viewLifecycleOwner) { userInfo ->
            binding.mainTitle.setText("Bienvenue ${userInfo.firstName} ${userInfo.lastName}")
            val avatar = userInfo.avatar ?: "https://prod-ripcut-delivery.disney-plus.net/v1/variant/disney/B57471ACFC0EAFD14A3B96A02DE1DF94DBAEF0634BFDCEAEE85F2B435217243C/scale?width=1200&aspectRatio=1.78&format=jpeg"
            binding.avatar.load(avatar){
                transformations(CircleCropTransformation())
            }
        }



        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = myAdapter
        //myAdapter.submitList(taskList.toList())
        val openAddActivitButton =binding.addTaskButton
        openAddActivitButton.setOnClickListener {
            val intent = Intent(activity,TaskActivity::class.java)
            intent.putExtra(putExtraPageKey,"Add a task")
            startForTaskResult.launch(intent)
        }
        
        binding.avatar.setOnClickListener() {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }


        myAdapter.onDeleteTask = {
            lifecycleScope.launch {
                viewModel.deleteTask(it)
            }

        }

        myAdapter.onEditTask = {
            val intent = Intent(activity,TaskActivity::class.java)
            intent.putExtra(putExtraPageKey,"Edit a task")
            intent.putExtra("task_id",it.id)
            intent.putExtra("task_title",it.title)
            intent.putExtra("task_description",it.description)
            startForTaskResult.launch(intent)
        }
    }

}
