package com.blazebooks.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.blazebooks.R
import com.blazebooks.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_sing_in.*
import kotlinx.android.synthetic.main.nav_header_main.*

class SingInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth; //Necesario para la autenticación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in)
        auth= FirebaseAuth.getInstance()
    }

    fun singInClicked(view: View) {


        if(singInActivityUserName.text.toString().isEmpty()){ //Comprueba que el campo username no está vacío
            singInActivityUserName.error = "Username cannot be empty"
            singInActivityUserName.requestFocus()
            return
        }


        if(singInActivityUserPasswd.text.toString().isEmpty()){ //Comprueba que el campo password no está vacío
            singInActivityUserPasswd.error = "Password cannot be empty"
            singInActivityUserPasswd.requestFocus()
            return

        } else if(singInActivityUserPasswd.text.toString().length<6){ //Comprueba que el campo password tiene más de 6 caracteres (Hace falta para validarlo con firebase
            singInActivityUserPasswd.error = "Password must be at least 6 characters long."
            singInActivityUserPasswd.requestFocus()
            return
        }

        if(singInActivityUserPasswdAux.text.toString() != singInActivityUserPasswd.text.toString()){ //Comprueba que el campo password coincide con el password aux
            singInActivityUserPasswdAux.error = "Passwords don't match"
            singInActivityUserPasswdAux.requestFocus()
            return
        }


        if(singInActivityUserEmail.text.toString().isEmpty()){ //Comprueba que el campo email no está vacío
            singInActivityUserEmail.error = "Email cannot be empty"
            singInActivityUserEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(singInActivityUserEmail.text.toString()).matches()){ //Comprueba que el campo email tiene el formato válido
            singInActivityUserEmail.error = "Please enter valid email"
            singInActivityUserEmail.requestFocus()
            return
        }

        // Crea el usuario en la base de Firebase y si está bien pasa directo al main
        auth.createUserWithEmailAndPassword(singInActivityUserEmail.text.toString(), singInActivityUserPasswd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updates = UserProfileChangeRequest.Builder().setDisplayName(singInActivityUserName.text.toString()).build()
                    auth.currentUser?.updateProfile(updates)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Snackbar.make(
                        view,
                        "Authentication failed.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }//if
            }

    }//singInClicked

    private fun userExist(): Boolean {
        return singInActivityUserName.text.toString() != "whoami"
    }

    private fun userFormat(): Boolean {
        return userExist() && singInActivityUserPasswd.text.toString() == singInActivityUserPasswdAux.text.toString()
    }

    fun goBack(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
