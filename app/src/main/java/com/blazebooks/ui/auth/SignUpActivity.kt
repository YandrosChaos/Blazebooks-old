package com.blazebooks.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.R
import com.blazebooks.databinding.ActivitySignInBinding
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignUpActivity : PreconfiguredActivity(), AuthListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        val viewModel: AuthViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.authListener = this
    }

    override fun onStartAuth() {
    }

    override fun onSuccessAuth(currentUser: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onEmailFaliure(errorStringCode: Int) {
        singInActivityUserEmail.apply {
            error = getString(errorStringCode)
            requestFocus()
        }
    }

    override fun onPasswordFaliure(errorStringCode: Int) {
        if (errorStringCode != R.string.signin_passwd_not_match) {
            singInActivityUserPasswd.apply {
                error = getString(errorStringCode)
                requestFocus()
            }
        } else {
            singInActivityUserPasswdAux.apply {
                error = getString(errorStringCode)
                requestFocus()
            }
        }
    }

    override fun onUsernameFaliure(errorStringCode: Int) {
        signInActivityUserName.apply {
            error = getString(errorStringCode)
            requestFocus()
        }
    }
}
