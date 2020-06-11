package com.blazebooks.ui.auth.login

import android.content.Intent

interface LoginNavigation {
    fun startActivityForResult(intent: Intent?, requestCode: Int)
}