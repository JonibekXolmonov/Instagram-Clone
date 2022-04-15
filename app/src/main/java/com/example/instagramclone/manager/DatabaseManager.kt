package com.example.instagramclone.manager

import com.example.instagramclone.manager.handler.DBUserHandler
import com.example.instagramclone.manager.handler.DBUsersHandler
import com.example.instagramclone.model.User
import com.google.firebase.firestore.FirebaseFirestore

private var USER_PATH = "users"
private var POST_PATH = "posts"
private var FEED_PATH = "feeds"
private var FOLLOWING_PATH = "following"
private var FOLLOWERS_PATH = "followers"

object DatabaseManager {

    private var database = FirebaseFirestore.getInstance()

    fun storeUser(user: User, handler: DBUserHandler) {
        database.collection(USER_PATH).document(user.uid).set(user)
            .addOnSuccessListener {
                handler.onSuccess(user)
            }
            .addOnFailureListener {
                handler.onError(it)
            }
    }

    fun loadUser(uid: String, handler: DBUserHandler) {
        database.collection(USER_PATH).document(uid).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val fullname = it.getString("fullname")
                    val email = it.getString("email")
                    val userImg = it.getString("userImg")

                    val user = User(fullname!!, email!!, userImg!!)
                    user.uid = uid
                    handler.onSuccess(user)
                } else {
                    handler.onSuccess(null)
                }
            }
            .addOnFailureListener {
                handler.onError(it)
            }
    }

    fun loadUsers(handler: DBUsersHandler) {
        database.collection(USER_PATH).get().addOnCompleteListener {
            val users = ArrayList<User>()

            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val uid = document.getString("uid")
                    val fullname = document.getString("fullname")
                    val email = document.getString("email")
                    val userImg = document.getString("userImg")

                    val user = User(fullname!!, email!!, userImg!!)
                    user.uid = uid!!
                    users.add(user)
                }
                handler.onSuccess(users)
            } else {
                handler.onError(it.exception!!)
            }
        }
    }

    fun updateUserImg(image: String) {
        val uid = AuthManager.currentUser()!!.uid
        database.collection(USER_PATH).document(uid).update("userImg", image)
    }
}