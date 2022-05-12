package com.example.instagramclone.model

data class FirebaseRequest(
    val notification: Notification,
    val registration_ids: List<String>,
    val data: Data
)

data class Notification(
    val body: String,
    val title: String
)

data class Data(val type: String)
