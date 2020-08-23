package com.example.bunker

class PlayerModel {
    var username: String? = null
    var image_drawable: Int = 0

    fun getUsernames(): String {
        return username.toString()
    }

    fun setUsernames(username: String) {
        this.username = username
    }

    fun getImage_drawables(): Int {
        return image_drawable
    }

    fun setImage_drawables(image_drawable: Int) {
        this.image_drawable = image_drawable
    }
}