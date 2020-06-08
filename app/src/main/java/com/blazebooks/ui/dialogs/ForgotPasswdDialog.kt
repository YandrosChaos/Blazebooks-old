package com.blazebooks.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.blazebooks.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_forgot_passwd.view.*
import kotlinx.android.synthetic.main.dialog_forgot_passwd.view.forgotpwdDialogUserName
import java.lang.ClassCastException

/**
 * Creates a new instance of ForgotPasswdDialog.
 *
 * @author Victor Gonzalez
 */
class ForgotPasswdDialog(private val auth: FirebaseAuth) : DialogFragment() {

    interface ForgotPasswdDialogListener {
        fun onForgotPasswdSent(dialog: ForgotPasswdDialog)
        fun onCloseForgotPasswdDialog(dialog: ForgotPasswdDialog)
    }

    private lateinit var listener: ForgotPasswdDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ForgotPasswdDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ForgotPasswdDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_forgot_passwd, container, false)
    }

    /**
     * Sets the onClickListeners to the buttons.
     *
     * @author Victor Gonzalez
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //send new passwd button listener
        //if EditText is empty or the email is not well-formatted, shows an error message.
        //else send the email and send the info to host activity
        view.forgotPasswdBtn.setOnClickListener {
            if (view.forgotpwdDialogUserName.text.isNotBlank()) {
                auth.sendPasswordResetEmail(view.forgotpwdDialogUserName.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            listener.onForgotPasswdSent(this)
                        } else {
                            view.forgotpwdDialogUserName.error =
                                getString(R.string.signin_email_not_valid)
                        }
                    }
            } else {
                view.forgotpwdDialogUserName.error = getString(R.string.log_email_error)
                view.forgotpwdDialogUserName.requestFocus()
            }

        }

        //close button listener
        view.forgotPasswdBtnExit.setOnClickListener {
            listener.onCloseForgotPasswdDialog(this)
        }
    }

}