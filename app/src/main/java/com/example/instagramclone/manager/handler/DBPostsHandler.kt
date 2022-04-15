package com.example.instagramclone.manager.handler

import com.example.instagramclone.model.Post
import com.example.instagramclone.model.User
import java.lang.Exception

interface DBPostsHandler {
    fun onSuccess(posts: ArrayList<Post>)
    fun onError(e: Exception)
}