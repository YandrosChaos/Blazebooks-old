package com.blazebooks.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.blazebooks.R
import com.blazebooks.data.models.User
import com.blazebooks.PreconfiguredActivity
import com.blazebooks.ui.settings.sharedpreferences.SharedPreferencesFragment
import com.blazebooks.util.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Shows the preferences that user can change into the app.
 *
 * @see SharedPreferencesFragment
 * @author Victor Gonzalez
 */
class SettingsActivity : PreconfiguredActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loadSettingsMainView()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     * Also loads the new config
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        loadNewConfig()
        toast(getString(R.string.should_restart))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    /**
     * Preload the app language selected by the user. If none selected, then uses the phone
     * language.
     *
     * @see PreconfiguredActivity
     * @author  Victor Gonzalez
     * @author  Mounir Zbayr
     */
    private fun loadNewConfig() {
        when (sharedPreferences.getString(
            LANGUAGE_SETTING_KEY,
            DEFAULT_LANGUAGE
        )) {
            "Spanish" -> defaultLocale = Locale("es")
            "English" -> defaultLocale = Locale("en")
        }

        //Es
        val newUsername = sharedPreferences.getString(NEW_USERNAME_KEY, "")
        val newEmail = sharedPreferences.getString(NEW_EMAIL_KEY, "")
        val newPassword = sharedPreferences.getString(NEW_PASSWD_KEY, "")
        if (!newUsername.equals("") || !newEmail.equals("") || !newPassword.equals(""))
            updateUserData(newUsername, newEmail, newPassword)


        sharedPreferences.edit().remove(NEW_USERNAME_KEY).apply()
        sharedPreferences.edit().putString(NEW_USERNAME_KEY, "").apply()
        sharedPreferences.edit().putString(NEW_PASSWD_KEY, "").apply()
    }//loadNewConfig

    /**
     * Comprueba el campo que ha sido modificado y actualiza los datos del usuario
     *
     * @author Mounir Zbayr
     */
    private fun updateUserData(newUsername: String?, newEmail: String?, newPassword: String?) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("Users").document(firebaseUser?.uid.toString()).get()
            .addOnSuccessListener { documentSnapshot ->
                val oldUser = documentSnapshot.toObject(User::class.java)

                //TODO -> val credential = EmailAuthProvider
                    //.getCredential(oldUser?.email.toString(), oldUser?.password.toString())
                //firebaseUser?.reauthenticate(credential)

                if (!newUsername.equals("")) {
                    oldUser?.userName = newUsername.toString()
                    toast("Username updated")
                }

                if (!newEmail.equals("")) {
                    oldUser?.email = newEmail.toString()
                    toast("Email updated")
                }

                if (!newPassword.equals("")) {
                    //TODO -> oldUser?.password = newPassword.toString()
                    toast("Password updated")
                }

                if (oldUser != null) {
                    //TODO -> UserDao().update(oldUser)
                }
            }

    }

    private fun loadSettingsMainView() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right)
            .replace(R.id.settingsFrameLayout,
                SharedPreferencesFragment()
            )
            .commit()
    }
}