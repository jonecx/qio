package com.jonecx.qio

import androidx.test.espresso.web.webdriver.DriverAtoms.webClick
import androidx.test.espresso.web.webdriver.Locator
import com.jonecx.qio.support.BaseUiAutomator
import org.junit.Test
class LoginTest : BaseUiAutomator() {

    @Test
    fun testLogin() {
        waitForText("Log in", 200)
        webEnterText(Locator.NAME, "email", BuildConfig.TEST_ACCOUNT)
        webEnterText(Locator.NAME, "password", BuildConfig.TEST_ACCOUNT_PASSWORD)
        webClick(Locator.CSS_SELECTOR, "button.TRX6J[aria-label='Log in']")
        waitForText(R.string.nav_bar_home, 3000)
    }
}
