package com.example.instagramclone.manager

import com.example.instagramclone.manager.handler.AuthHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthManager {
    companion object {
        private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        fun currentUser(): FirebaseUser? {
            return firebaseAuth.currentUser
        }

        fun isSignedIn(): Boolean {
            return currentUser() != null
        }

        fun signIn(email: String, password: String, handler: AuthHandler) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = currentUser()!!.uid
                        handler.onSuccess(uid)
                    } else {
                        handler.onError(task.exception)
                    }
                }
        }

        fun signUp(email: String, password: String, handler: AuthHandler) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = currentUser()!!.uid
                        handler.onSuccess(uid)
                    } else {
                        handler.onError(task.exception)
                    }
                }
        }

        fun signOut() {
            firebaseAuth.signOut()
        }
    }
}