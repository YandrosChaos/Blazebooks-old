package com.blazebooks.ui.settings.sharedpreferences

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.blazebooks.R
import com.blazebooks.data.dataAccessObjects.UserDao
import com.blazebooks.util.DELETE_ACCOUNT_KEY

/**
 * @author Victor Gonzalez
 */
class SharedPreferencesFragment : PreferenceFragmentCompat() {
    private lateinit var deleteAccountButton: Preference
    private lateinit var setProfileImageButton: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteAccountButton = findPreference(DELETE_ACCOUNT_KEY)!!

        deleteAccountButton.setOnPreferenceClickListener {
            val alert = createDeleteDialog()
            alert.setTitle(getString(R.string.delete_account_dialog))
            alert.show()
            true
        }
    }

    private fun createDeleteDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)

        // set message of alert dialog
        dialogBuilder.setMessage(getString(R.string.delete_account_dialog_desc))
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton(getString(R.string.delete_account_dialog_confirm)) { dialog, id ->
                UserDao().delete("id")
            }
            // negative button text and action
            .setNegativeButton(getString(R.string.delete_account_dialog_cancel)) { dialog, id ->
                dialog.cancel()
            }
        return dialogBuilder.create()
    }

}