package com.blazebooks.ui.preferences

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.dataAccessObjects.UserDao
import com.blazebooks.model.User
import com.blazebooks.ui.PreconfiguredActivity
import com.blazebooks.ui.preferences.SettingsActivity.SettingsFragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/**
 * Shows the preferences that user can change into the app.
 *
 * @see SettingsFragment
 * @author Victor Gonzalez
 */
class SettingsActivity : PreconfiguredActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    /**
     * Destroys this activity. Also loads the selected language.
     *
     * @see PreconfiguredActivity
     * @author Victor Gonzalez
     */
    override fun onDestroy() {
        super.onDestroy()
        loadNewConfig()
        Toast.makeText(
            this,
            getString(R.string.should_restart),
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Preload the app language selected by the user. If none selected, then uses the phone
     * language.
     *
     * @see PreconfiguredActivity
     * @author  Victor Gonzalez & Mounir Zbayr
     */
    private fun loadNewConfig() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString(
            Constants.LANGUAGE_SETTING_KEY,
            Constants.DEFAULT_LANGUAGE
        )) {
            "Spanish" -> defaultLocale = Locale("es")
            "English" -> defaultLocale = Locale("en")
        }

        //Es
        val newUsername = sharedPreferences.getString(Constants.NEW_USERNAME_KEY, "")
        val newEmail = sharedPreferences.getString(Constants.NEW_EMAIL_KEY, "")
        val newPassword = sharedPreferences.getString(Constants.NEW_PASSWD_KEY, "")
        if (!newUsername.equals("") || !newEmail.equals("") || !newPassword.equals(""))
                updateUserData(newUsername, newEmail, newPassword)


        sharedPreferences.edit().putString(Constants.NEW_EMAIL_KEY, "").apply()
        sharedPreferences.edit().putString(Constants.NEW_USERNAME_KEY, "").apply()
        sharedPreferences.edit().putString(Constants.NEW_PASSWD_KEY, "").apply()
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

                Toast.makeText(this, oldUser?.toString(), Toast.LENGTH_LONG).show()

                val credential = EmailAuthProvider
                    .getCredential(oldUser?.email.toString(), oldUser?.password.toString())
                firebaseUser?.reauthenticate(credential)

                if (!newUsername.equals("")) oldUser?.userName = newUsername.toString()

                if (!newEmail.equals("")) oldUser?.email = newEmail.toString()

                if (!newPassword.equals("")) oldUser?.password = newPassword.toString()

                if (oldUser != null) {
                    UserDao(this).update(oldUser)
                }
            }
    }

    /**
     * Returns to previous activity and sets custom animation transition.
     *
     * @author Victor Gonzalez
     */
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}