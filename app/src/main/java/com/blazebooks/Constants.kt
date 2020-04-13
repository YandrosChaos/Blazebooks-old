package com.blazebooks

import com.blazebooks.model.Book

class Constants {
    companion object {
        //GENERAL CONSTANTS
        const val CORNER_RADIOUS: Float = 60f

        //SPLASH ACTIVITY
        const val SPLASH_SCREEN_TIME_OUT = 4000

        //INTENT'S CODES
        const val TOOLBAR_TITLE_CODE = "TOOLBAR_TITLE_CODE"

        //CURRENT DATA
        lateinit var CURRENT_BOOK: Book

    }
}