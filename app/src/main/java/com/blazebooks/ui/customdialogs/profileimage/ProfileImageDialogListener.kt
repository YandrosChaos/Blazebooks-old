package com.blazebooks.ui.customdialogs.profileimage

interface ProfileImageDialogListener {
    fun onReturnImageSelected(dialog: ProfileImageDialog)
    fun onExitProfileImageDialog(dialog: ProfileImageDialog)
    fun onCleanProfileImage(dialog: ProfileImageDialog)
}