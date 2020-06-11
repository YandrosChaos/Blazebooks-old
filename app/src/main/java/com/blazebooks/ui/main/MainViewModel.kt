package com.blazebooks.ui.main

import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.util.startLoginActivity

class MainViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }
}