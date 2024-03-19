package com.jonecx.qio.utils

import android.content.res.AssetManager
import java.io.IOException
import java.net.URLEncoder

fun String.encode(): String = URLEncoder.encode(this, "UTF-8")

fun String?.orBlank() = if (this.isNullOrBlank()) "" else this

fun getRandomString(lengthOfString: Int = 10): String {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..lengthOfString)
        .map { charPool.random() }
        .joinToString("")
}

fun AssetManager.readJsonAsString(fileName: String): String {
    return try {
        val inputStream = open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    } ?: ""
}
