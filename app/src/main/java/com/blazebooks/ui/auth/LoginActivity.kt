package com.blazebooks.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.R
import com.blazebooks.data.models.User
import com.blazebooks.databinding.ActivityLoginBinding
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdDialog
import com.blazebooks.ui.customdialogs.forgotpassword.ForgotPasswdDialogListener
import com.blazebooks.util.snackbar
import com.blazebooks.util.startMainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

const val GOOGLE_SIGN_IN = 1984

class LoginActivity : PreconfiguredActivity(), ForgotPasswdDialogListener,
    AuthListener, KodeinAware {
    override val kodein by kodein()
    private val factory by instance<AuthViewModelFactory>()
    private lateinit var viewModel: AuthViewModel

    /**
     * @author Víctor González
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this
    }

    /**
     * Login with a Google account
     *
     * @author Victor Gonzalez
     */
    fun loginByGoogle(view: View) {
        loginActivityLoadingSKV.visibility = View.VISIBLE
        val googleClient = GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
        //salir de la sesion actual de Google
        googleClient.signOut()

        //activity para loguearse con google
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    /**
     * Login with google account results
     *
     * @author Victor Gonzalez
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            try {
                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)

                if (account != null) {
                    viewModel.auth.signInWithCredential(
                        GoogleAuthProvider.getCredential(
                            account.idToken,
                            null
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {

                            //añadir nuevo usuario a la collection users
                            FirebaseFirestore.getInstance().collection("Users")
                                .document(viewModel.auth.currentUser?.uid.toString())
                                .set(
                                    User(
                                        viewModel.auth.currentUser!!.displayName.toString(),
                                        "unknown",
                                        viewModel.auth.currentUser!!.email.toString(),
                                        "https://example.com/jane-q-user/profile.jpg",
                                        false
                                    )
                                )

                            onSuccessAuth()
                        } else {
                            loginActivityLoadingSKV.visibility = View.GONE
                            Toast.makeText(
                                baseContext,
                                getString(R.string.log_general_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    loginActivityLoadingSKV.visibility = View.GONE
                }
            } catch (e: ApiException) {
                loginActivityLoadingSKV.visibility = View.GONE
                Toast.makeText(
                    baseContext,
                    getString(R.string.google_auth_err),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
            )
            .commit()
    }

    /**
     * Receives the answer from the dialog and closes it.
     *
     * @see ForgotPasswdDialog
     * @see ForgotPasswdDialog.ForgotPasswdDialogListener
     * @see onCloseForgotPasswdDialog
     *
     * @author Victor Gonzalez
     */
    override fun onForgotPasswdSent(dialog: ForgotPasswdDialog) {
        login_root_layout.snackbar(getString(R.string.log_dialog_email_sent))
    }

    /**
     * Closes the dialog.
     *
     * @see ForgotPasswdDialog
     * @see ForgotPasswdDialog.ForgotPasswdDialogListener
     * @see onForgotPasswdSent
     *
     * @author Victor Gonzalez
     */
    override fun onCloseForgotPasswdDialog(dialog: ForgotPasswdDialog) {
        dialog.dismiss()
        loginActivityForgotPasswdFL.visibility = View.GONE
    }

    override fun onStartAuth() {
        loginActivityForgotPasswdFL.visibility = View.VISIBLE
    }

    override fun onSuccessAuth() {
        loginActivityLoadingSKV.visibility = View.GONE
        startMainActivity()
    }

    override fun onFailureAuth(message: String) {
        loginActivityForgotPasswdFL.visibility = View.GONE
        login_root_layout.snackbar(message)
    }

    override fun onEmailFaliure(errorStringCode: Int) {
    }

    override fun onPasswordFaliure(errorStringCode: Int) {
    }

    override fun onUsernameFaliure(errorStringCode: Int) {
    }
}