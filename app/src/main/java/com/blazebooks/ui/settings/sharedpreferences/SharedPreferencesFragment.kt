package com.blazebooks.ui.settings.sharedpreferences

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.blazebooks.R
import com.blazebooks.util.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

/**
 * @author Victor Gonzalez
 */
class SharedPreferencesFragment : PreferenceFragmentCompat(), KodeinAware {
    override val kodein by kodein()
    private val controller by instance<SharedPrefController>()

    private lateinit var deleteAccountButton: Preference
    private lateinit var setUsernameButton: Preference
    private lateinit var setEmailButton: Preference
    private lateinit var setPassButton: Preference


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteAccountButton = findPreference(DELETE_ACCOUNT_KEY)!!
        setUsernameButton = findPreference(NEW_USERNAME_KEY)!!
        setEmailButton = findPreference(NEW_EMAIL_KEY)!!
        setPassButton = findPreference(NEW_PASSWD_KEY)!!

        setUsernameButton.setOnPreferenceClickListener {
            createSetUsernameDialog().show()
            true
        }

        setEmailButton.setOnPreferenceClickListener {
            createSetEmailDialog().show()
            true
        }

        setPassButton.setOnPreferenceClickListener {
            createSetPassDialog().show()
            true
        }

        deleteAccountButton.setOnPreferenceClickListener {
            createDeleteDialog().show()
            true
        }
    }

    /**
     * Creates an alert dialog witch deletes the current user account.
     * If user choose "yes" option, then close all activities and
     * start the login activity.
     *
     * @author Victor Gonzalez
     */
    private fun createDeleteDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)

        // set message of alert dialog
        dialogBuilder.setMessage(getString(R.string.delete_account_dialog_desc))
            .setCancelable(false)
            .setTitle(getString(R.string.delete_account_dialog))
            .setPositiveButton(getString(R.string.delete_account_dialog_confirm)) { _, _ ->
                lifecycleScope.launch {
                    controller.deleteAccount()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            //success
                            controller.logout()
                            startLoginActivity()
                        }, {
                            //faliure
                            toast(it.message!!)
                        })
                }
            }
            .setNegativeButton(getString(R.string.delete_account_dialog_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return dialogBuilder.create()
    }

    /**
     * Creates an alert dialog that ask about a new username.
     *
     * @author Victor Gonzalez
     */
    private fun createSetUsernameDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_item_edit_text, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)

        dialogBuilder
            .setTitle("New Username")
            .setView(dialogLayout)
            .setPositiveButton("OK") { _, _ ->
                editText.text?.let {
                    if (it.isNotEmpty()) {

                        lifecycleScope.launch {
                            controller.updateUsername(it.toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()

                        }

                    } else {
                        toast("Write something!")
                    }
                }
            }
            .setNegativeButton(getString(R.string.delete_account_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        return dialogBuilder.create()
    }

    private fun createSetEmailDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_item_edit_text, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)

        dialogBuilder
            .setTitle("New Email")
            .setView(dialogLayout)
            .setPositiveButton("OK") { _, _ ->
                editText.text?.let {editText ->
                    if (
                        editText.isNotEmpty()
                        || !Patterns.EMAIL_ADDRESS.matcher(editText.toString()).matches()
                    ) {

                        lifecycleScope.launch {
                            controller.updateEmail(editText.toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    //success
                                    toast("Success!")
                                }, {
                                    //faliure
                                    toast(it.message!!)
                                })

                        }

                    } else {
                        toast("Write something!")
                    }
                }
            }
            .setNegativeButton(getString(R.string.delete_account_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        return dialogBuilder.create()
    }

    private fun createSetPassDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_item_edit_text, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.editText)

        dialogBuilder
            .setTitle("New Password")
            .setView(dialogLayout)
            .setPositiveButton("OK") { _, _ ->
                editText.text?.let { editText ->
                    if (editText.isNotEmpty()) {

                        lifecycleScope.launch {
                            controller.updatePassword(editText.toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    //success
                                    toast("Success!")
                                }, {
                                    //faliure
                                    toast(it.message!!)
                                })

                        }

                    } else {
                        toast("Write something!")
                    }
                }
            }
            .setNegativeButton(getString(R.string.delete_account_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        return dialogBuilder.create()
    }


}