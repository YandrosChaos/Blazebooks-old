package com.blazebooks.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.PremiumRepository

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val LoginRepo: LoginRepository,
    private val premiumRepo: PremiumRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(LoginRepo, premiumRepo) as T
    }

}