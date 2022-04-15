package com.example.instagramclone.utils

import android.util.Log

object Logger {
    val IS_TESTER = true
    fun d(tag: String, msg: String) {
        if (IS_TESTER) Log.d(tag, msg)
    }

    fun i(tag: String, msg: String) {
        if (IS_TESTER) Log.d(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (IS_TESTER) Log.d(tag, msg)
    }

    fun e(tag: String, msg: String) {
        if (IS_TESTER) Log.d(tag, msg)
    }
}