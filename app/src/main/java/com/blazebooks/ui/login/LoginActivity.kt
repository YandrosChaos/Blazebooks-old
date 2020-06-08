package com.blazebooks.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.blazebooks.util.Constants
import com.blazebooks.R
import com.blazebooks.data.dataAccessObjects.UserDao
import com.blazebooks.databinding.ActivityLoginBinding
import com.blazebooks.model.User
import com.blazebooks.ui.main.MainActivity
import com.blazebooks.model.PreconfiguredActivity
import com.blazebooks.ui.dialogs.ForgotPasswdDialog
import com.blazebooks.util.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

const val GOOGLE_SIGN_IN = 1984

class LoginActivity : PreconfiguredActivity(), ForgotPasswdDialog.ForgotPasswdDialogListener,
    AuthListener {

    private lateinit var auth: FirebaseAuth //Necesario para la autenticación

    /**
     * @param savedInstanceState
     * @author Mounir Zbayr
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.authListener = this
        auth = FirebaseAuth.getInstance()
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
                    auth.signInWithCredential(
                        GoogleAuthProvider.getCredential(
                            account.idToken,
                            null
                        )
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {

                            //añadir nuevo usuario a la collection users
                            FirebaseFirestore.getInstance().collection("Users")
                                .document(auth.currentUser?.uid.toString())
                                .set(
                                    User(
                                        auth.currentUser!!.displayName.toString(),
                                        "unknown",
                                        auth.currentUser!!.email.toString(),
                                        "https://example.com/jane-q-user/profile.jpg",
                                        false
                                    )
                                )

                            onSuccessAuth(auth.currentUser)
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
        onSuccessAuth(auth.currentUser)
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
            .replace(R.id.loginActivityForgotPasswdFL, ForgotPasswdDialog(auth))
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
        toast(getString(R.string.log_dialog_email_sent))
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

    /**
     * Throws SignInActivity and finish this activity
     *
     * @param view
     * @author Victor Gonzalez
     */
    fun throwSignInActivity(view: View) {
        startActivity(Intent(this, SignInActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        finish()
    }

    override fun onStartAuth() {

    }

    /**
     * Si el usuario no es nulo, se pasa al main.
     * Si lo es, muestra un mensaje de error
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    override fun onSuccessAuth(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            Constants.CURRENT_USER = UserDao().get(currentUser.uid)
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
            finish()
        } else {
            toast(getString(R.string.log_general_error))
        }
    }

    override fun onEmailFaliure() {
        loginActivityUserName.error = getString(R.string.log_email_error)
        loginActivityUserName.requestFocus()
    }

    override fun onPasswordFaliure() {
        loginActivityUserPasswd.error = getString(R.string.log_passwd_error)
        loginActivityUserPasswd.requestFocus()
    }
}
