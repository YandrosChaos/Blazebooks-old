package com.blazebooks

import com.blazebooks.model.Book

class Constants {
    companion object {

        //INTENT'S CODES
        const val TOOLBAR_TITLE_CODE = "TOOLBAR_TITLE_CODE"

        //CURRENT DATA
        lateinit var CURRENT_BOOK: Book

        //SETTINGS KEYS
        const val LANGUAGE_SETTING_KEY = "language"
        const val DEFAULT_LANGUAGE = "English"
        const val GENRES_SETTING_KEY="preferredGenres"
        const val READ_MODE_KEY = "readMode"
        const val SOUND_MODE_KEY = "soundMode"

    }
}