package com.blazebooks.ui.customdialogs.forgotpassword

/**
 * Communicates the Dialog with the host activity.
 *
 * @author Victor Gonzalez
 */
interface ForgotPasswdDialogListener {
    fun onForgotPasswdSent(dialog: ForgotPasswdDialog)
    fun onCloseForgotPasswdDialog(dialog: ForgotPasswdDialog)
}