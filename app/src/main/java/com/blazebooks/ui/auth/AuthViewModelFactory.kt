package com.blazebooks.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.LoginRepository

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val LoginRepo: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(LoginRepo) as T
    }

}