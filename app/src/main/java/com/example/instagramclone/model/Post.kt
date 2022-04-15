package com.example.instagramclone.model

import java.text.SimpleDateFormat
import java.util.*

class Post {
    var id: String = ""
    var caption: String = ""
    var image: String = ""
    var postImage: String = ""
    var currentDate: String = currentTime()

    var uid: String = ""
    var fullname: String = ""
    var userImg: String = ""

    constructor(caption: String, postImage: String) {
        this.caption = caption
        this.postImage = postImage
    }


    constructor(id: String, caption: String, postImage: String, currentDate: String) {
        this.id = id
        this.caption = caption
        this.postImage = postImage
        this.currentDate = currentDate
    }

    constructor()


    private fun currentTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")
        return sdf.format(Date())
    }
}