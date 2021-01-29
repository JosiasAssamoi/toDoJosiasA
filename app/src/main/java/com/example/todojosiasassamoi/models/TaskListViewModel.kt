package com.example.todojosiasassamoi.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todojosiasassamoi.network.TasksRepository
import com.example.todojosiasassamoi.tasklist.Task
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

    fun loadTasks() {

        viewModelScope.launch {
            val fetchedTasks = repository.loadTasks()
            if(fetchedTasks != null) _taskList.value = fetchedTasks!!
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            val successfull = repository.removeTask(task)
            loadTasks()
        }

    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            val successfull = repository.createTask(task)
            loadTasks()
        }
    }
    fun editTask(updatedLocalTask: Task) {
        viewModelScope.launch {
            val successfull = repository.updateTask(updatedLocalTask)
            if (successfull) {
                val editableList = _taskList.value.orEmpty().toMutableList()
                val position = editableList.indexOfFirst { updatedLocalTask.id == it.id }
                editableList[position] = updatedLocalTask
                _taskList.value = editableList
            }

        }
    }
}