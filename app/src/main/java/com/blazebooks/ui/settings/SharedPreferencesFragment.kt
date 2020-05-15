package com.blazebooks.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.blazebooks.Constants
import com.blazebooks.R

/**
 * @author Victor Gonzalez
 */
class SharedPreferencesFragment() : PreferenceFragmentCompat() {
    private lateinit var deleteAccountButton: Preference
    private lateinit var setProfileImageButton: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteAccountButton = findPreference(Constants.DELETE_ACCOUNT_KEY)!!
        setProfileImageButton = findPreference(Constants.SELECTED_PROFILE_IMAGE_KEY)!!

        setProfileImageButton.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), "Not implemented yet.",Toast.LENGTH_SHORT).show()
            true
        }

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
                //TODO -> DELETE ACCOUNT?
            }
            // negative button text and action
            .setNegativeButton(getString(R.string.delete_account_dialog_cancel)) { dialog, id ->
                dialog.cancel()
            }
        return dialogBuilder.create()
    }

}