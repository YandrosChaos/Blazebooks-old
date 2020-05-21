package com.blazebooks.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.blazebooks.Constants
import com.blazebooks.R
import com.blazebooks.dataAccessObjects.UserDao
import com.blazebooks.model.User
import com.blazebooks.ui.PreconfiguredActivity
import com.blazebooks.ui.dialogs.ProfileImageDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_settings.*
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
        Toast.makeText(
            this,
            getString(R.string.should_restart),
            Toast.LENGTH_LONG
        ).show()
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


        sharedPreferences.edit().remove(Constants.NEW_USERNAME_KEY).apply()
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

                val credential = EmailAuthProvider
                    .getCredential(oldUser?.email.toString(), oldUser?.password.toString())
                firebaseUser?.reauthenticate(credential)

                if (!newUsername.equals("")) {
                    oldUser?.userName = newUsername.toString()
                    Toast.makeText(this, "Username updated", Toast.LENGTH_SHORT).show()
                }

                if (!newEmail.equals("")) {
                    oldUser?.email = newEmail.toString()
                    Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                }

                if (!newPassword.equals("")){
                    oldUser?.password = newPassword.toString()
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                }

                if (oldUser != null) {
                    UserDao().update(oldUser)
                }
            }

    }

    private fun loadSettingsMainView() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right)
            .replace(R.id.settingsFrameLayout, SharedPreferencesFragment())
            .commit()
    }
}