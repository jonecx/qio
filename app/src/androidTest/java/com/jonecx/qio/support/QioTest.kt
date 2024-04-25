package com.jonecx.qio.support

import android.app.Instrumentation
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule

open class QioTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    protected val appPackageName = "com.jonecx.qio"
    protected val LAUNCH_TIMEOUT = 5000L
    protected val DEFAULT_TIMEOUT = 250L
    protected var context: Context = ApplicationProvider.getApplicationContext()

    protected val instrumentRegistry: Instrumentation = InstrumentationRegistry.getInstrumentation()

    protected fun webClick(locator: Locator, textTag: String) {
        Web.onWebView()
            .withElement(DriverAtoms.findElement(locator, textTag))
            .perform(DriverAtoms.webClick())
    }

    protected fun webEnterText(locator: Locator, textTag: String, inputText: String) {
        Web.onWebView()
            .withElement(DriverAtoms.findElement(locator, textTag))
            .perform(DriverAtoms.webKeys(inputText))
    }

    protected fun assertTag(testTag: String) {
        composeTestRule
            .onNodeWithTag(testTag)
            .assertExists()
    }

    protected fun setContent(composable: @Composable () -> Unit) {
        composeTestRule.setContent(composable)
    }
}
