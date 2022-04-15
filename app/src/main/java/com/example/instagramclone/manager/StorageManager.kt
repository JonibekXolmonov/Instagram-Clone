package com.example.instagramclone.manager

import android.net.Uri
import com.example.instagramclone.manager.handler.StorageHandler
import com.google.firebase.storage.FirebaseStorage

private var USER_PHOTO_PATH = "users"
private var POST_PHOTO_PATH = "posts"

object StorageManager {
    private val storage = FirebaseStorage.getInstance()
    var storageRef = storage.reference

    fun uploadUserPhoto(uri: Uri, handler: StorageHandler) {
        val filename = AuthManager.currentUser()!!.uid + ".png"
        val uploadTask = storageRef.child(USER_PHOTO_PATH).child(filename).putFile(uri)
        uploadTask.addOnSuccessListener { task ->
            val result = task.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener { url ->
                val imgUrl = url.toString()
                handler.onSuccess(imgUrl)
            }.addOnFailureListener { e ->
                handler.onError(e)
            }
        }.addOnFailureListener { e ->
            handler.onError(e)
        }
    }
}