package com.blazebooks.ui.customdialogs.forgotpassword

import androidx.lifecycle.ViewModel
import com.blazebooks.data.repositories.LoginRepository

class ForgotPasswdViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    fun sendEmail(email: String) = repository.sendPasswdReset(email)

}