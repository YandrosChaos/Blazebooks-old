package com.blazebooks.ui.auth

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.R
import com.blazebooks.databinding.ActivitySignInBinding
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.util.snackbar
import com.blazebooks.util.startMainActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : PreconfiguredActivity(), AuthListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        val viewModel: AuthViewModel =
            ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.authListener = this
    }

    override fun onStartAuth() {
        //TODO -> add progress bar!
    }

    override fun onSuccessAuth() {
        //TODO -> quit progress bar!
        startMainActivity()
    }

    override fun onFailureAuth(message: String) {
        root_signup_layout.snackbar(message)
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
