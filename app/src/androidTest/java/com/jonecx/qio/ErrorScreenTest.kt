package com.jonecx.qio

import com.jonecx.qio.feature.authentication.ErrorScreen
import com.jonecx.qio.support.QioTest
import org.junit.Test

class ErrorScreenTest : QioTest() {
    @Test
    fun errorScreenDisplayed() {
        setContent {
            ErrorScreen()
        }

        assertTag("qio_error_screen")
    }
}
