package com.jonecx.qio.utils

import java.net.URLEncoder

fun String.encode(): String = URLEncoder.encode(this, "UTF-8")

fun String?.orBlank() = if (this.isNullOrBlank()) "" else this

fun getRandomString(lengthOfString: Int = 10): String {
    val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..lengthOfString)
        .map { charPool.random() }
        .joinToString("")
}
