package com.blazebooks.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.R
import com.blazebooks.databinding.ActivityLoginBinding
import com.blazebooks.databinding.ActivitySignInBinding
import com.blazebooks.model.PreconfiguredActivity
import com.blazebooks.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : PreconfiguredActivity(), AuthListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        val viewModel: AuthViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.authListener = this
    }

    /**
     * Calls onBackPressed method.
     *
     * @see onBackPressed
     * @see LoginActivity
     * @author  Victor Gonzalez
     */
    fun goToLogin(view: View) {
        onBackPressed()
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        finish()
    }

    override fun onStartAuth() {
    }

    override fun onSuccessAuth(currentUser: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onEmailFaliure(errorStringCode: Int) {
        singInActivityUserEmail.error = getString(errorStringCode)
        singInActivityUserEmail.requestFocus()
    }

    override fun onPasswordFaliure(errorStringCode: Int) {
        if (errorStringCode != R.string.signin_passwd_not_match) {
            singInActivityUserPasswd.error = getString(errorStringCode)
            singInActivityUserPasswd.requestFocus()
        } else {
            singInActivityUserPasswdAux.error = getString(errorStringCode)
            singInActivityUserPasswdAux.requestFocus()
        }
    }

    override fun onUsernameFaliure(errorStringCode: Int) {
        signInActivityUserName.error = getString(errorStringCode)
        signInActivityUserName.requestFocus()
    }
}
