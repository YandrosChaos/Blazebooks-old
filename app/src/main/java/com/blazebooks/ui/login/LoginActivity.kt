package com.blazebooks.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.blazebooks.R
import com.blazebooks.dataAccessObjects.UserDao
import com.blazebooks.model.User
import com.blazebooks.ui.MainActivity
import com.blazebooks.ui.PreconfiguredActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.dialog_forgot_passwd.view.*

enum class ProviderType {
    BASIC,
    GOOGLE
}

class LoginActivity : PreconfiguredActivity() {

    private lateinit var auth: FirebaseAuth //Necesario para la autenticación
    private val GOOGLE_SIGN_IN = 1984

    /**
     * @param savedInstanceState
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }

    /**
     *
     * @author Mounir Zbayr
     */
    fun loginClicked(view: View) {

        if (loginActivityUserName.text.toString().isEmpty()) {
            loginActivityUserName.error = getString(R.string.log_email_error)
            loginActivityUserName.requestFocus()
            return
        }

        if (loginActivityUserPasswd.text.toString().isEmpty()) {
            loginActivityUserPasswd.error = getString(R.string.log_passwd_error)
            loginActivityUserPasswd.requestFocus()
            return
        }


        //Comprueba que los datos coinciden y si es así actualiza el updateUI con el usuario , si no lo pone a nulo
        auth.signInWithEmailAndPassword(
            loginActivityUserName.text.toString(),
            loginActivityUserPasswd.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateUI(auth.currentUser)
                } else {
                    updateUI(null)
                    Toast.makeText(
                        baseContext, getString(R.string.log_general_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }//loginClicked

    /**
     * Si el usuario no es nulo, se pasa al main.
     * Si lo es, muestra un mensaje de error
     *
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.zoom_in, R.anim.static_animation)
            finish()
        }
    }

    /**
     * Login with Google account
     *
     * @author Victor Gonzalez
     */
    fun loginByGoogle(view: View) {
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

                            updateUI(auth.currentUser)
                        } else {
                            Toast.makeText(
                                baseContext,
                                getString(R.string.log_general_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: ApiException) {
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
    public override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }//onStart

    /**
     * Sends an email for reset the password.
     *
     * @param view
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    @SuppressLint("InflateParams")
    fun sendPasswordResetEmail(view: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_passwd, null)
        //show dialog
        val mAlertDialog = AlertDialog
            .Builder(this)
            .setView(mDialogView)
            .show()

        //set button onClickListener
        mDialogView.forgotPasswdBtn.setOnClickListener {
            if (mDialogView.forgotpwdDialogUserName.text.isNotEmpty()) {
                auth.sendPasswordResetEmail(mDialogView.forgotpwdDialogUserName.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            mAlertDialog.dismiss()
                            Toast.makeText(
                                this,
                                getString(R.string.log_dialog_email_sent),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, getString(R.string.log_dialog_email_error), Toast.LENGTH_LONG)
                    .show()
            }
        }
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
}
