package com.blazebooks.ui.customdialogs.profileimage

/**
 * Connect the dialog with the host activity.
 *
 * @author Victor Gonzalez
 */
interface ProfileImageDialogListener {
    fun onReturnImageSelected(dialog: ProfileImageDialog)
    fun onExitProfileImageDialog(dialog: ProfileImageDialog)
    fun onCleanProfileImage(dialog: ProfileImageDialog)
}