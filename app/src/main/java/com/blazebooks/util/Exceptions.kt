package com.blazebooks.util

import java.io.IOException
import java.lang.Exception

class FirestoreDatabaseException(message: String, exception: Exception?) :
    IOException(message, exception)

class NoInternetException(message: String) : IOException(message)
class PremiumAccountNotFoundException(message: String) : IOException(message)