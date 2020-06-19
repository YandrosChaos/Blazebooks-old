package com.blazebooks.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.databinding.ActivityLoginBinding
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdDialog
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdDialogListener
import com.blazebooks.util.*
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

const val GOOGLE_SIGN_IN = 1984

class LoginActivity : PreconfiguredActivity(), ForgotPasswdDialogListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding

    /**
     * Comprueba al iniciar si el usuario es nulo. Si lo es se carga el resto
     * del login y si no pasa directo al main. También establece los eventos para
     * los diferentes botones de la vista.
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        viewModel.getCurrentUser()?.let {
            startMainActivity()
        }

        //login button
        binding.buttonLogin.setOnClickListener {
            loginUser(it)
        }
        //login with google button
        binding.buttonLoginGoogle.setOnClickListener {
            loginWithGoogle()
        }
        //go to signup activity
        binding.textViewSignUp.setOnClickListener {
            startSignUpActivity()
        }
        //forgotPasswd button
        binding.textViewForgotPassword.setOnClickListener {
            onForgotPasswordDialog()
        }
    }

    /**
     * Creates a new instance of ForgotPasswdDialog.
     *
     * @see ForgotPasswdDialog
     *
     * @author Victor Gonzalez
     */
    private fun onForgotPasswordDialog() {
        loginActivityForgotPasswdFL.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_top)
            .replace(
                R.id.loginActivityForgotPasswdFL,
                ForgotPasswdDialog()
            ).commit()
    }

    private fun loginUser(view: View) {
        val email = binding.loginActivityUserName.text.toString().trim()
        val passwd = binding.loginActivityUserPasswd.text.toString().trim()

        //validations
        if (email.isEmpty()
            || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            || passwd.isEmpty()
        ) {
            view.snackbar(getString(R.string.invalid_email_or_passwd))
            return
        }


        loginActivityLoadingSKV.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                viewModel.userLogin(email, passwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //success call
                        startMainActivity()
                        finish()
                    }, {
                        //failure call
                        view.snackbar(it.message!!)
                    })
            } catch (e: ApiException) {
                view.snackbar("Check your internet connection please.")
            }
        }
        loginActivityLoadingSKV.visibility = View.GONE
    }

    /**
     * Login with a Google account
     *
     * @author Victor Gonzalez
     */
    private fun loginWithGoogle() {
        loginActivityLoadingSKV.visibility = View.VISIBLE
        val client = viewModel.getGoogleClient(applicationContext)
        //salir de la sesion actual de Google
        client.signOut()

        //start google login activity
        startActivityForResult(
            client.signInIntent,
            GOOGLE_SIGN_IN
        )
        loginActivityLoadingSKV.visibility = View.GONE
    }

    /**
     * Login with google account results
     *
     * @author Victor Gonzalez
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            binding.loginActivityLoadingSKV.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val account = viewModel.getGoogleAccount(data)

                    if (account != null) {
                        viewModel.loginWithGoogle(account)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                //success callback
                                startMainActivity()
                            }, {
                                //failure callback
                                binding.root.snackbar("Check your internet connection please.")
                            })
                    } else {
                        binding.root.snackbar("Account does not exist!")
                    }
                } catch (e: ApiException) {
                }
            }
            binding.loginActivityLoadingSKV.visibility = View.GONE
        }
    }

    /**
     * Receives the answer from the dialog and closes it.
     *
     * @see ForgotPasswdDialog
     * @see onCloseForgotPasswdDialog
     *
     * @author Victor Gonzalez
     */
    override fun onForgotPasswdSent(dialog: ForgotPasswdDialog) {
        login_root_layout.snackbar(getString(R.string.log_dialog_email_sent))
        onCloseForgotPasswdDialog(dialog)
    }

    /**
     * Closes the dialog.
     *
     * @see ForgotPasswdDialog
     *
     * @author Victor Gonzalez
     */
    override fun onCloseForgotPasswdDialog(dialog: ForgotPasswdDialog) {
        hideKeyboard()
        dialog.dismiss()
        loginActivityForgotPasswdFL.visibility = View.GONE
    }

}