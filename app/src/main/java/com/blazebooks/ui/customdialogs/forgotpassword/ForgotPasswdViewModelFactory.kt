package com.blazebooks.ui.customdialogs.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.LoginRepository

@Suppress("UNCHECKED_CAST")
class ForgotPasswdViewModelFactory (
    private val repository: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgotPasswdViewModel(repository) as T
    }

}