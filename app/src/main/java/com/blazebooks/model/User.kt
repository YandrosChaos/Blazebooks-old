package com.blazebooks.model


data class User(
    val userName: String
    , val password: String
    , val email: String
    , val URLprofile: String
    , var premium: Boolean
) {

}