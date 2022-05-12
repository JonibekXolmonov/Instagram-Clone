package com.example.instagramclone.service

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.instagramclone.fragment.HomeFragment
import com.example.instagramclone.utils.Logger
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val FOLLOW = "follow"
const val UNFOLLOW = "unfollow"

class FCMService : FirebaseMessagingService() {

    val TAG = FCMService::class.java.simpleName

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.i(TAG, "Refreshed Token:: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Logger.i(TAG, "Message: ${message.notification}")

        val type = message.data["type"]
        Logger.i(TAG, "Type: $type")
        lateinit var intent: Intent

        when (type) {
            FOLLOW -> {
                intent = Intent(this, HomeFragment::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("type", "follow")
            }
            UNFOLLOW -> {
                intent = Intent(Intent.ACTION_VIEW).also {
                    it.data =
                        Uri.parse("https://play.google.com/store/apps/details?id=ic0der.justwater")
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
        }
    }
}