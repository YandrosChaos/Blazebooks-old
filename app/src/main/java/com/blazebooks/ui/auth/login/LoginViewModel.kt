package com.blazebooks.ui.auth.login

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.ui.auth.signup.SignUpActivity
import com.blazebooks.util.LiveMessageEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val repository: LoginRepository
) : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val startActivityForResultEvent = LiveMessageEvent<LoginNavigation>()

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        repository.currentUser()
    }

    var email: String? = null
    var passwd: String? = null
    var authListener: LoginListener? = null

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
     * Throws SignUpActivity
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
     *  Disposing the disposables.
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /**
     * Login with a Google account
     *
     * @author Victor Gonzalez
     */
    fun googleSignUp(view: View) {
        authListener?.onStartAuth()

        val googleClient = GoogleSignIn.getClient(
            view.context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(view.resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

        //salir de la sesion actual de Google
        googleClient.signOut()

        startActivityForResultEvent.sendEvent {
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    /**
     * Login with google account results
     *
     * @author Victor Gonzalez
     */
    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGN_IN) {
            try {
                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(data)
                    .getResult(ApiException::class.java)

                if (account != null) {

                    val disposable = repository.loginWithGoogle(account)
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
/*
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
                                )*/
                }
            } catch (e: ApiException) {
                authListener?.onFailureAuth("Google authentication error")
            }
        }
    }

}