package com.blazebooks.data.models

data class User(
    var userName: String = "",
    var password: String = "",
    var email: String = "",
    var URLprofile: String = "",
    var premium: Boolean = false
) {

    fun toMap(): Map<String, Any> {
        return HashMap<String, Any>(
            hashMapOf(
                "userName" to userName,
                "password" to password,
                "email" to email,
                "URLprofile" to URLprofile,
                "premium" to premium
            )
        )
    }

    override fun toString(): String {
        return "User(userName='$userName', password='$password', email='$email', URLprofile='$URLprofile', premium=$premium)"
    }
}