package com.blazebooks.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.LoginRepository

@Suppress("UNCHECKED_CAST")
class SignupViewModelFactory(
    private val repository: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignupViewModel(repository) as T
    }

}