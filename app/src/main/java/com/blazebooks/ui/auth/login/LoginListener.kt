package com.blazebooks.ui.auth.login

interface LoginListener {
    fun onSuccessAuth()
    fun onStartAuth()
    fun onFailureAuth(message: String)
}