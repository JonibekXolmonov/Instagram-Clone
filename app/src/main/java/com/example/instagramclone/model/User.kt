package com.example.instagramclone.model

class User {
    var uid: String = ""
    var fullname: String = ""
    var email: String = ""
    var password: String = ""
    var userImg: String = ""
    var isFollowed: Boolean = false

    var deviceTokens: ArrayList<String> = ArrayList()

    constructor(fullname: String, email: String) {
        this.fullname = fullname
        this.email = email
    }


    constructor(fullname: String, email: String, image: String) {
        this.fullname = fullname
        this.email = email
        this.userImg = image
    }

    constructor(fullname: String, email: String, password: String, image: String) {
        this.fullname = fullname
        this.email = email
        this.password = password
        this.userImg = image
    }

    constructor()

    constructor(fullname: String, email: String, deviceTokens: ArrayList<String>) {
        this.fullname = fullname
        this.email = email
        this.deviceTokens = deviceTokens
    }
}