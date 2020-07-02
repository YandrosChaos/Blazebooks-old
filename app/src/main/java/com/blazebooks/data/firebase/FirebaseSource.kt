package com.blazebooks.data.firebase

import android.net.Uri
import com.blazebooks.util.DEFAULT_PROFILE_IMAGE
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.Completable
import com.google.firebase.auth.UserProfileChangeRequest.Builder

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

    fun delete() = Completable.create { emitter ->
        currentFirebaseUser()!!.delete().addOnCompleteListener { task ->
            if (!emitter.isDisposed) {
                if (task.isSuccessful) emitter.onComplete()
                else emitter.onError(task.exception!!)
            }
        }

    }

    fun updateUserName(name: String) = Completable.create { emitter ->
        val profileUpdates = Builder().setDisplayName(name)
            .build()

        firebaseAuth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun updatePhotoUri(uri: String?) = Completable.create { emitter ->
        var newImage = uri
        val user = currentFirebaseUser()

        if(newImage.isNullOrEmpty()){
            newImage = DEFAULT_PROFILE_IMAGE
        }

        val profileUpdates = Builder()
            .setDisplayName(user!!.displayName)
            .setPhotoUri(Uri.parse(newImage))
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }

    }

    fun updateEmail(email: String) = Completable.create { emitter ->
        firebaseAuth.currentUser?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun updatePassword(passwd: String) = Completable.create { emitter ->
        firebaseAuth.currentUser?.updatePassword(passwd)
            ?.addOnCompleteListener { task ->
                if (!emitter.isDisposed) {
                    if (task.isSuccessful) emitter.onComplete()
                    else emitter.onError(task.exception!!)
                }
            }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentFirebaseUser() = firebaseAuth.currentUser

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

    fun sendPasswordReset(email: String) = Completable.create { emitter ->
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) emitter.onComplete()
                else emitter.onError(it.exception!!)
            }
        }
    }
}