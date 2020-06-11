package com.blazebooks.ui.auth.signup

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.ui.auth.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignupViewModel(
    private val repository: LoginRepository
) : ViewModel(){

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        repository.currentUser()
    }

    var username: String? = null
    var email: String? = null
    var passwd: String? = null
    var repeatPasswd: String? = null
    var signupListener: SignupListener? = null

    /**
     *  @author Mounir
     *  @author Victor Gonzalez
     */
    fun singupClicked(view: View) {
        if (username.isNullOrEmpty()) {
            //Comprueba que el campo username no está vacío
            signupListener?.onUsernameFaliure(R.string.signin_username_error)
            return
        }

        if (passwd.isNullOrEmpty()) {
            //Comprueba que el campo password no está vacío
            signupListener?.onPasswordFaliure(R.string.signin_passwd_empty)
            return
        } else if (passwd!!.length < 6) {
            //Comprueba que el campo password tiene más de 6 caracteres (Hace falta para validarlo con firebase
            signupListener?.onPasswordFaliure(R.string.signin_passwd_length)
            return
        }
        if (repeatPasswd.isNullOrEmpty() || passwd != repeatPasswd) {
            //Comprueba que el campo password coincide con el password aux
            signupListener?.onPasswordFaliure(R.string.signin_passwd_not_match)
            return
        }

        if (email.isNullOrEmpty()) {
            //Comprueba que el campo email no está vacío
            signupListener?.onEmailFaliure(R.string.signin_email_empty)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            //Comprueba que el campo email tiene el formato válido
            signupListener?.onEmailFaliure(R.string.signin_email_not_valid)
            return
        }

        signupListener?.onStartAuth()
        val disposable = repository.register(email!!, passwd!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                signupListener?.onSuccessAuth()
            }, {
                signupListener?.onFailureAuth(it.message!!)
            })
        disposables.add(disposable)
        //Creacion e insercion del usuario en la base de datos
        /*
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

         */
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

    /**
     *  Disposing the disposables.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}