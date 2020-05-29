package com.blazebooks

import com.blazebooks.model.Book
import com.blazebooks.model.User

class Constants {
    companion object {

        //INTENT'S CODES
        const val PATH_CODE = "PATH_CODE"
        const val TOOLBAR_TITLE_CODE = "TOOLBAR_TITLE_CODE"

        //CURRENT DATA
        lateinit var CURRENT_BOOK: Book
        lateinit var CURRENT_USER : User

        //SHARED PREFERENCES KEYS
        const val LANGUAGE_SETTING_KEY = "language"
        const val DEFAULT_LANGUAGE = "English"
        const val GENRES_SETTING_KEY="preferredGenres"
        const val READ_MODE_KEY = "readMode"
        const val SOUND_MODE_KEY = "soundMode"
        const val NEW_USERNAME_KEY = "usernamePref"
        const val NEW_PASSWD_KEY = "passwdPref"
        const val NEW_EMAIL_KEY = "emailPref"
        const val SELECTED_PROFILE_IMAGE_KEY = "selectedProfileImg"
        const val DELETE_ACCOUNT_KEY = "deleteAccount"
        const val LAST_BOOK_SELECTED_KEY = "lastBookSelected"

    }
}