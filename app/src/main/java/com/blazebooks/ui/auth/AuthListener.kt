package com.blazebooks.ui.auth

import com.google.firebase.auth.FirebaseUser

interface AuthListener {
    fun onStartAuth()
    fun onSuccessAuth(currentUser: FirebaseUser?)
    fun onEmailFaliure(errorStringCode: Int)
    fun onPasswordFaliure(errorStringCode: Int)
    fun onUsernameFaliure(errorStringCode: Int)
}