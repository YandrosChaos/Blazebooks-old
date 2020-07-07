package com.blazebooks.ui.auth


import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.blazebooks.R
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.data.repositories.PremiumRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author Victor Gonzalez
 */
class AuthViewModel(
    private val loginRepo: LoginRepository,
    private val premiumRepo: PremiumRepository
) : ViewModel() {

    fun getCurrentUser() = loginRepo.currentUser()

    /**
     * Login a user with an email and pass.
     *
     * @param email
     * @param passwd
     */
    suspend fun userLogin(
        email: String,
        passwd: String
    ) = withContext(Dispatchers.IO) { loginRepo.login(email, passwd) }

    /**
     * Gets the googleClient.
     *
     * @param context
     */
    fun getGoogleClient(
        context: Context
    ): GoogleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    )

    /**
     * Login a user with an google account.
     *
     * @param account
     */
    suspend fun loginWithGoogle(
        account: GoogleSignInAccount
    ) = withContext(Dispatchers.IO) { loginRepo.loginWithGoogle(account) }

    /**
     * Gets the current google account.
     *
     * @param data
     */
    suspend fun getGoogleAccount(
        data: Intent?
    ) = withContext(Dispatchers.IO) {
        GoogleSignIn
            .getSignedInAccountFromIntent(data)
            .getResult(ApiException::class.java)
    }

    /**
     * Sign up an user to the Firestore database.
     *
     * @param email
     * @param password
     */
    suspend fun signUp(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) { loginRepo.register(email, password) }

    suspend fun isPremium() =
        withContext(Dispatchers.IO) { premiumRepo.getPremiumUid(loginRepo.currentUser()!!.uid) }


}