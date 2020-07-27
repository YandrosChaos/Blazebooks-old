package com.blazebooks.util

import java.io.IOException
import java.lang.Exception

class FirestoreDatabaseException(message: String, exception: Exception?) :
    IOException(message, exception)

class PremiumAccountNotFoundException(message: String) : IOException(message)
class DocumentNotFoundException(message: String) : IOException(message)