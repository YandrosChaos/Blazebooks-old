package com.blazebooks.ui.main

import androidx.lifecycle.ViewModel
import com.blazebooks.data.preferences.PreferenceProvider
import com.blazebooks.data.repositories.LoginRepository
import com.blazebooks.ui.auth.LoginActivity

private const val DEFAULT_USERNAME_VALUE = ""

class MainViewModel(
    private val repository: LoginRepository,
    private val prefs: PreferenceProvider
) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    val urlBook: String? = prefs.getLastBook()
    var username: String = user?.displayName ?: DEFAULT_USERNAME_VALUE
    var usermail: String = user?.email.toString()


    /**
     *  Signs out from the current session and clean the SharedPreferences.
     *
     * @see LoginActivity
     *
     * @author Mounir
     * @author Victor Gonzalez
     */
    fun logout() {
        repository.logout()
        prefs.clearAll()
    }

    fun getStoredProfileImage() = (prefs.userImage() ?: user?.photoUrl).toString()

}