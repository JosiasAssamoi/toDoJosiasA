package com.example.todojosiasassamoi.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todojosiasassamoi.userInfo.UserInfo
import com.example.todojosiasassamoi.network.UserInfoRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private val _userInfos = MutableLiveData<UserInfo>()
    val userInfos: LiveData<UserInfo> = _userInfos

    fun loadInfo() {
        viewModelScope.launch {
           val  user = repository.loadInfo()
            _userInfos.value = user!!
        }

    }

    fun updateAvatar(avatar: MultipartBody.Part){
        viewModelScope.launch {
            repository.updateAvatar(avatar)
            _userInfos.value = repository.loadInfo()
        }
    }
    fun update(userInfo: UserInfo){
        viewModelScope.launch {
            repository.edit(userInfo)
            _userInfos.value = repository.loadInfo()

        }
    }

}
