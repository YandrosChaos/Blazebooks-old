package com.blazebooks.model

import android.content.Context
import android.view.View

data class User(
    val userName: String
    , val password: String
    , val email: String
    , val URLprofile: String
    , var premium: Boolean
) {


}