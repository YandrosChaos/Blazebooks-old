package com.blazebooks.util

import com.blazebooks.data.models.Book
import com.blazebooks.data.models.User


//INTENT'S CODES
const val PATH_CODE = "PATH_CODE"
const val TOOLBAR_TITLE_CODE = "TOOLBAR_TITLE_CODE"

//CURRENT DATA
var PREMIUM = false
lateinit var CURRENT_BOOK: Book;

//SHARED PREFERENCES KEYS
const val LANGUAGE_SETTING_KEY = "language"
const val DEFAULT_LANGUAGE = "English"
const val GENRES_SETTING_KEY = "preferredGenres"
const val READ_MODE_KEY = "readMode"
const val SOUND_MODE_KEY = "soundMode"
const val NEW_USERNAME_KEY = "usernamePref"
const val NEW_PASSWD_KEY = "passwdPref"
const val NEW_EMAIL_KEY = "emailPref"
const val DELETE_ACCOUNT_KEY = "deleteAccount"
const val  LAST_BOOK_SELECTED_KEY = "lastBookSelected"
const val PREMIUM_ACCOUNT_KEY = "premiumAccount"

//Default values
const val DEFAULT_USERNAME_VALUE = "Blazebooks User"
const val DEFAULT_PROFILE_IMAGE =
    "https://cdn.pixabay.com/photo/2013/07/13/11/34/owl-158411_960_720.png"