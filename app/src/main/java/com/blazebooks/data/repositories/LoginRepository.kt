package com.blazebooks.data.repositories

import com.blazebooks.data.firebase.FirebaseSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

/**
 * Llama a las funciones dentro de FirebaseSource
 *
 * @see FirebaseSource
 * @author Victor Gonzalez
 */
class LoginRepository(
    private val firebase: FirebaseSource
) {
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun loginWithGoogle(account: GoogleSignInAccount) = firebase.loginWithGoogle(account)
}