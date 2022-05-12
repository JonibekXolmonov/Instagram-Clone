package com.example.instagramclone.networking

import com.example.instagramclone.model.FirebaseRequest
import com.example.instagramclone.model.FirebaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Authorization:$ACCESS_KEY")
    @POST("send")
    fun sendNotification(@Body firebaseRequest: FirebaseRequest): Call<FirebaseResponse>

    companion object {
        const val ACCESS_KEY =
            "key=AAAABQ98go0:APA91bE53NIg8z2JisFyoq7W73ZVyvG3lm-UgJdIv5XtTyBhUIWNBBn3RYftUz96S_S3JVEwuvNw8DEBS6xHya9R-SRz6bR0QZMTelqYq3jmcFzl9RD7mY_5Q2f0U3oaq-43EQkL26d3"
    }
}