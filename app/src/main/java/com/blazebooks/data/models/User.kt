package com.blazebooks.data.models

data class User(
    var userID: String = "",
    var userName: String = "",
    var email: String = "",
    var premium: Boolean = false
) {

    override fun toString(): String {
        return "User(userName='$userName', email='$email', premium=$premium)"
    }
}