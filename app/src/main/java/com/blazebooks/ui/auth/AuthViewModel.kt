package com.blazebooks.ui.auth


import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.repositories.LoginRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val loginRepo: LoginRepository
) : ViewModel() {

    fun getCurrentUser() = loginRepo.currentUser()

    suspend fun userLogin(
        email: String,
        passwd: String
    ) = withContext(Dispatchers.IO) { loginRepo.login(email, passwd) }

    fun getGoogleClient(
        context: Context
    ): GoogleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    suspend fun loginWithGoogle(
        account: GoogleSignInAccount
    ) = withContext(Dispatchers.IO) { loginRepo.loginWithGoogle(account) }

    suspend fun getGoogleAccount(
        data: Intent?
    ) = withContext(Dispatchers.IO) {
        GoogleSignIn
            .getSignedInAccountFromIntent(data)
            .getResult(ApiException::class.java)
    }

    suspend fun signUp(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) { loginRepo.register(email, password) }

}