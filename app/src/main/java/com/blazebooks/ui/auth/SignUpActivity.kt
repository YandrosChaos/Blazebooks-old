package com.blazebooks.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blazebooks.R
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.databinding.ActivitySignUpBinding
import com.blazebooks.util.snackbar
import com.blazebooks.util.startLoginActivity
import com.blazebooks.util.startMainActivity
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : PreconfiguredActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        //sign up button
        binding.buttonSignUp.setOnClickListener {
            userSignUp(it)
        }
        //return to login button
        binding.editTextLoginActivity.setOnClickListener {
            startLoginActivity()
            finish()
        }

    }

    private fun userSignUp(view: View) {
        val email = binding.signInActivityUserEmail.text.toString().trim()
        val pass = binding.signInActivityUserPasswd.text.toString().trim()
        val repeatPass = binding.signInActivityUserPasswdAux.text.toString().trim()

        //validations
        if (pass.isEmpty()) {
            binding.signInActivityUserPasswd.error = getString(R.string.signin_passwd_empty)
            binding.signInActivityUserPasswd.requestFocus()
            return
        } else if (pass.length < 6) {
            binding.signInActivityUserPasswd.error = getString(R.string.signin_passwd_length)
            binding.signInActivityUserPasswd.requestFocus()
            return
        }
        if (repeatPass.isEmpty() || pass != repeatPass) {
            binding.signInActivityUserPasswdAux.error = getString(R.string.signin_passwd_not_match)
            binding.signInActivityUserPasswdAux.requestFocus()
            return
        }
        if (email.isEmpty()) {
            binding.signInActivityUserEmail.error = getString(R.string.signin_email_empty)
            binding.signInActivityUserEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.signInActivityUserEmail.error = getString(R.string.signin_email_not_valid)
            binding.signInActivityUserEmail.requestFocus()
            return
        }

        lifecycleScope.launch {
            try {
                viewModel.signUp(email, pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //success
                        startMainActivity()
                        finish()
                    }, {
                        //failure
                        view.snackbar(it.message!!)
                    })
            } catch (e: ApiException) {
                view.snackbar(e.message!!)
            }
        }

    }

}
