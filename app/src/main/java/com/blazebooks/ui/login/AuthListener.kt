package com.blazebooks.ui.login

import com.google.firebase.auth.FirebaseUser

interface AuthListener {
    fun onStartAuth()
    fun onSuccessAuth(currentUser: FirebaseUser?)
    fun onEmailFaliure()
    fun onPasswordFaliure()
}