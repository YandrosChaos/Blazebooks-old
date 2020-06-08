package com.blazebooks.ui.login

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * @author Victor Gonzalez
 */
class AuthViewModel : ViewModel(){

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var email: String? = null
    var passwd: String? = null
    var authListener: AuthListener? = null

    /**
     *
     * @author Mounir Zbayr
     */
    fun loginClicked(view: View){
        authListener?.onStartAuth()

        if(email.isNullOrEmpty()){
            authListener?.onEmailFaliure()
            return
        }

        if(passwd.isNullOrEmpty()){
            authListener?.onPasswordFaliure()
            return
        }

        //Comprueba que los datos coinciden y si es así actualiza con el usuario , si no lo pone a nulo
        auth.signInWithEmailAndPassword(
            email!!,
            passwd!!
        ).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    authListener?.onSuccessAuth(auth.currentUser!!)
                } else {
                    authListener?.onSuccessAuth(null)
                }
            }
    }

}