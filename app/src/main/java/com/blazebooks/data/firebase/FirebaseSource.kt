package com.blazebooks.data.firebase

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable

/**
 * Completable es una clase de RxJava que permite obtener una indicación cuando
 * está completado o cuando a fallado. Esto es perfecto porque la autenticacion es una
 * operacion de red, y así podremos saber si se ha completado correctamente o no.
 *
 * Usamos el emitter para indicar si el task se ha completado.
 *
 * @author Victor Gonzalez
 */
class FirebaseSource {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun login(email: String, passwd: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, passwd: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun loginWithGoogle(account: GoogleSignInAccount) = Completable.create { emitter ->
        firebaseAuth.signInWithCredential(
            GoogleAuthProvider.getCredential(
                account.idToken,
                null
            )
        ).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }

        }
    }

}