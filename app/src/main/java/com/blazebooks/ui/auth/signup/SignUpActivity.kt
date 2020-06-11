package com.blazebooks.ui.auth.signup

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.databinding.ActivitySignUpBinding
import com.blazebooks.util.snackbar
import com.blazebooks.util.startMainActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : PreconfiguredActivity(),
    SignupListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<SignupViewModelFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySignUpBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        val viewModel: SignupViewModel =
            ViewModelProviders.of(this, factory).get(SignupViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.signupListener = this
    }

    override fun onStartAuth() {
        signupActivityLoadingSKV.visibility = View.VISIBLE
    }

    override fun onSuccessAuth() {
        signupActivityLoadingSKV.visibility = View.GONE
        startMainActivity()
    }

    override fun onFailureAuth(message: String) {
        signupActivityLoadingSKV.visibility = View.GONE
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
