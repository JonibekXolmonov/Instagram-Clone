package com.example.instagramclone.manager.handler

import java.lang.Exception

interface AuthHandler {
    fun onSuccess(uid:String)
    fun onError(exception: Exception?)
}