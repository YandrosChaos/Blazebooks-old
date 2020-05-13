package com.blazebooks.model


data class User(
    var userName: String = ""
    , var password: String= ""
    , var email: String= ""
    , var URLprofile: String= ""
    , var premium: Boolean= false
) {
    override fun toString(): String {
        return "User(userName='$userName', password='$password', email='$email', URLprofile='$URLprofile', premium=$premium)"
    }
}