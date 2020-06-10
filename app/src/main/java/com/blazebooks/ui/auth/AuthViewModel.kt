package com.blazebooks.ui.auth

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.dataAccessObjects.UserDao
import com.blazebooks.data.models.User
import com.blazebooks.data.repositories.UserRepository
import com.blazebooks.util.CURRENT_USER
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * @author Victor Gonzalez
 */
class AuthViewModel(
    private val repository: UserRepository
) : ViewModel() {

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
    var authListener: AuthListener? = null

    /**
     * @author Mounir Zbayr
     * @author Victor Gonzalez
     */
    fun loginClicked(view: View) {

        //email validations
        if (email.isNullOrEmpty()) {
            authListener?.onFailureAuth(view.resources.getString(R.string.invalid_email_or_passwd))
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email!!).matches()) {
            //Comprueba que el campo email tiene el formato válido
            authListener?.onFailureAuth(view.resources.getString(R.string.invalid_email_or_passwd))
            return
        }
        //passwd validation
        if (passwd.isNullOrEmpty()) {
            authListener?.onFailureAuth(view.resources.getString(R.string.invalid_email_or_passwd))
            return
        }
        authListener?.onStartAuth()

        val disposable = repository.login(email!!, passwd!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //success callback
                authListener?.onSuccessAuth()
            }, {
                //failure callback
                authListener?.onFailureAuth(it.message!!)
            })

        disposables.add(disposable)
    }

    /**
     *  @author Mounir
     *  @author Victor Gonzalez
     */
    fun singupClicked(view: View) {
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

        authListener?.onStartAuth()
        val disposable = repository.register(email!!, passwd!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccessAuth()
            }, {
                authListener?.onFailureAuth(it.message!!)
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

    /**
     *  Disposing the disposables.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}