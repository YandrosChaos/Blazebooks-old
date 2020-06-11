package com.blazebooks.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.databinding.ActivityLoginBinding
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdDialog
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdDialogListener
import com.blazebooks.util.hideKeyboard
import com.blazebooks.util.snackbar
import com.blazebooks.util.startMainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

const val GOOGLE_SIGN_IN = 1984

class LoginActivity : PreconfiguredActivity(), ForgotPasswdDialogListener,
    LoginListener, LoginNavigation, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<LoginViewModelFactory>()
    private lateinit var viewModel: LoginViewModel

    /**
     * @author Víctor González
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this
        //this sets the LifeCycler owner and receiver
        viewModel.startActivityForResultEvent.setEventReceiver(this, this)
    }

    /**
     * Login with google account results
     *
     * @author Victor Gonzalez
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onResultFromActivity(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Comprueba al iniciar si el usuario es nulo. Si lo es se muestra
     * la vista del login y si no pasa directo al main
     *
     * @author Mounir Zbayr
     */
    override fun onStart() {
        super.onStart()
        viewModel.user?.let {
            startMainActivity()
        }
    }//onStart

    /**
     * Creates a new instance of ForgotPasswdDialog.
     *
     * @see ForgotPasswdDialog
     * @param view
     *
     * @author Victor Gonzalez
     */
    fun sendPasswordResetEmail(view: View) {
        loginActivityForgotPasswdFL.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_to_top)
            .replace(
                R.id.loginActivityForgotPasswdFL,
                ForgotPasswdDialog(
                    viewModel.auth
                )
            ).commit()
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

    override fun onStartAuth() {
        loginActivityForgotPasswdFL.visibility = View.VISIBLE
    }

    override fun onSuccessAuth() {
        loginActivityLoadingSKV.visibility = View.GONE
        loginActivityUserName.isClickable = false
        loginActivityUserPasswd.isClickable = false
        startMainActivity()
    }

    override fun onFailureAuth(message: String) {
        loginActivityForgotPasswdFL.visibility = View.GONE
        login_root_layout.snackbar(message)
    }
}