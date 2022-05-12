package com.example.instagramclone.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    var BASE_URL = "https://fcm.googleapis.com/fcm/"



    fun getRetrofit(): Retrofit {
        val build = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        return build
    }

    fun <T> createService(service: Class<T>): T {
        return getRetrofit().create(service)
    }
}