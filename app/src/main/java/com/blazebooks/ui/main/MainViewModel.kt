package com.blazebooks.ui.main

import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.data.repositories.UserRepository
import com.blazebooks.util.startLoginActivity

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }
}