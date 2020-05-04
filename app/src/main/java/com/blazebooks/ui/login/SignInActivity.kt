package com.blazebooks.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.blazebooks.ui.PreconfiguredActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : PreconfiguredActivity() {

    private lateinit var auth: FirebaseAuth //Necesario para la autenticación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        this.loadHints()
        auth = FirebaseAuth.getInstance()
    }

    /**
     *
     *
     * @author Mounir Zbayr
     */
    fun singInClicked(view: View) {

        if (singInActivityUserName.text.toString()
                .isEmpty()
        ) { //Comprueba que el campo username no está vacío
            singInActivityUserName.error = getString(R.string.signin_username_error)
            singInActivityUserName.requestFocus()
            return
        }


        if (singInActivityUserPasswd.text.toString()
                .isEmpty()
        ) { //Comprueba que el campo password no está vacío
            singInActivityUserPasswd.error = getString(R.string.signin_passwd_empty)
            singInActivityUserPasswd.requestFocus()
            return

        } else if (singInActivityUserPasswd.text.toString().length < 6) { //Comprueba que el campo password tiene más de 6 caracteres (Hace falta para validarlo con firebase
            singInActivityUserPasswd.error = getString(R.string.signin_passwd_length)
            singInActivityUserPasswd.requestFocus()
            return
        }

        if (singInActivityUserPasswdAux.text.toString() != singInActivityUserPasswd.text.toString()) { //Comprueba que el campo password coincide con el password aux
            singInActivityUserPasswdAux.error = getString(R.string.signin_passwd_not_match)
            singInActivityUserPasswdAux.requestFocus()
            return
        }


        if (singInActivityUserEmail.text.toString()
                .isEmpty()
        ) { //Comprueba que el campo email no está vacío
            singInActivityUserEmail.error = getString(R.string.signin_email_empty)
            singInActivityUserEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(singInActivityUserEmail.text.toString())
                .matches()
        ) { //Comprueba que el campo email tiene el formato válido
            singInActivityUserEmail.error = getString(R.string.signin_email_not_valid)
            singInActivityUserEmail.requestFocus()
            return
        }

        // Crea el usuario en la base de Firebase y si está bien pasa directo al main
        auth.createUserWithEmailAndPassword(
            singInActivityUserEmail.text.toString(),
            singInActivityUserPasswd.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updates = UserProfileChangeRequest.Builder()
                        .setDisplayName(singInActivityUserName.text.toString()).build()
                    auth.currentUser?.updateProfile(updates)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(
                        view, task.exception?.message.toString()
                        ,
                        Snackbar.LENGTH_LONG
                    ).show()
                }//if
            }

    }//singInClicked

    /**
     * Returns to LoginActivity and finish this one.
     *
     * @see LoginActivity
     * @author  Victor Gonzalez
     */
    fun goToLogin(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    /**
     * Loads hints into textviews
     *
     * @author Victor Gonzalez
     */
    private fun loadHints() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            singInActivityUserName.setAutofillHints(View.AUTOFILL_HINT_USERNAME)
            singInActivityUserPasswd.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
            singInActivityUserPasswdAux.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
            singInActivityUserEmail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS)
        }
    }
}
