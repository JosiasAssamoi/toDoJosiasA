package com.example.todojosiasassamoi.network

import com.example.todojosiasassamoi.userInfo.UserInfo
import okhttp3.MultipartBody


class UserInfoRepository {
    private val userWebService = Api.userService

    suspend fun loadInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun edit(user: UserInfo): Boolean {
        val response = userWebService.update(user)
        return if (response.isSuccessful) true else false
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part): Boolean {
        val response = userWebService.updateAvatar(avatar)
        return if (response.isSuccessful) true else false
    }




}