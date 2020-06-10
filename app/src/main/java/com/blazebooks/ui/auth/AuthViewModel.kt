package com.blazebooks.ui.auth

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.dataAccessObjects.UserDao
import com.blazebooks.model.User
import com.blazebooks.util.CURRENT_USER
import com.google.firebase.auth.FirebaseAuth

/**
 * @author Victor Gonzalez
 */
class AuthViewModel : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var username: String? = null
    var email: String? = null
    var passwd: String? = null
    var repeatPasswd: String? = null
    var authListener: AuthListener? = null

    /**
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun loginClicked(view: View) {
        authListener?.onStartAuth()

        //email validations
        if (email.isNullOrEmpty()) {
            authListener?.onEmailFaliure(R.string.invalid_email_or_passwd)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            //Comprueba que el campo email tiene el formato válido
            authListener?.onEmailFaliure(R.string.invalid_email_or_passwd)
            return
        }

        //pass validations
        if (passwd.isNullOrEmpty()) {
            authListener?.onPasswordFaliure(R.string.invalid_email_or_passwd)
            return
        }

        //Comprueba que los datos coinciden y si es así actualiza con el usuario , si no lo pone a nulo
        auth.signInWithEmailAndPassword(
            email!!,
            passwd!!
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                authListener?.onSuccessAuth(auth.currentUser!!)
                UserDao().get(auth.currentUser!!.uid)
            } else {
                authListener?.onSuccessAuth(null)
            }
        }
    }

    /**
     *  @author Mounir
     *  @author Victor Gonzalez
     */
    fun singupClicked(view: View) {
        authListener?.onStartAuth()
        if (username.isNullOrEmpty()) {
            //Comprueba que el campo username no está vacío
            authListener?.onUsernameFaliure(R.string.signin_username_error)
            return
        }

        if (passwd.isNullOrEmpty()) {
            //Comprueba que el campo password no está vacío
            authListener?.onPasswordFaliure(R.string.signin_passwd_empty)
            return
        } else if (passwd!!.length < 6) {
            //Comprueba que el campo password tiene más de 6 caracteres (Hace falta para validarlo con firebase
            authListener?.onPasswordFaliure(R.string.signin_passwd_length)
            return
        }
        if (repeatPasswd.isNullOrEmpty() || passwd != repeatPasswd) {
            //Comprueba que el campo password coincide con el password aux
            authListener?.onPasswordFaliure(R.string.signin_passwd_not_match)
            return
        }

        if (email.isNullOrEmpty()) {
            //Comprueba que el campo email no está vacío
            authListener?.onEmailFaliure(R.string.signin_email_empty)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            //Comprueba que el campo email tiene el formato válido
            authListener?.onEmailFaliure(R.string.signin_email_not_valid)
            return
        }


        //Creacion e insercion del usuario en la base de datos
        val user = User(
            username!!,
            passwd!!,
            email!!,
            "https://example.com/jane-q-user/profile.jpg",
            false
        )

        UserDao().insert(user)
        CURRENT_USER = user
        authListener?.onSuccessAuth(null)
    }

    /**
     * Throws SignInActivity
     *
     * @param view
     * @author Victor Gonzalez
     */
    fun onSignUn(view: View) {
        Intent(view.context, SignUpActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    /**
     * Throws LoginActivity
     *
     * @param view
     * @author Victor Gonzalez
     */
    fun onLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

}