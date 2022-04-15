package com.example.instagramclone.manager.handler

import com.example.instagramclone.model.User
import java.lang.Exception

interface DBUsersHandler {
    fun onSuccess(users: ArrayList<User>)
    fun onError(e: Exception)
}