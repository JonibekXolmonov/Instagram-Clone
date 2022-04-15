package com.example.instagramclone.manager.handler

import com.example.instagramclone.model.Post
import java.lang.Exception

interface DBPostHandler {
    fun onSuccess(post: Post)
    fun onError(e: Exception)
}