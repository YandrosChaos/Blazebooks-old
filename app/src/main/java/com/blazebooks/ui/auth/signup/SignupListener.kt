package com.blazebooks.ui.auth.signup

interface SignupListener {
    fun onStartAuth()
    fun onSuccessAuth()
    fun onFailureAuth(message: String)
    fun onEmailFaliure(errorStringCode: Int)
    fun onPasswordFaliure(errorStringCode: Int)
    fun onUsernameFaliure(errorStringCode: Int)
}