package com.blazebooks.ui.customdialogs.forgotpassword

interface ForgotPasswdDialogListener {
    fun onForgotPasswdSent(dialog: ForgotPasswdDialog)
    fun onCloseForgotPasswdDialog(dialog: ForgotPasswdDialog)
}