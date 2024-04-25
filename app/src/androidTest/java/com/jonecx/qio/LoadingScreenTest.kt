package com.jonecx.qio

import com.jonecx.qio.feature.authentication.LoadingScreen
import com.jonecx.qio.support.QioTest
import org.junit.Test

class LoadingScreenTest : QioTest() {

    @Test
    fun loadingScreenDisplayed() {
        setContent {
            LoadingScreen()
        }

        assertTag("qio_loading_screen")
        assertTag("qio_indeterminate_circular_progress_bar")
    }
}
