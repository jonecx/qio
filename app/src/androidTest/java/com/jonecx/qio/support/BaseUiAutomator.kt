package com.jonecx.qio.support

import android.content.Intent
import androidx.annotation.StringRes
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import junit.framework.TestCase.assertFalse
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class BaseUiAutomator : QioTest() {

    private var device: UiDevice = UiDevice.getInstance(instrumentRegistry)

    @Before
    fun launchQio() {
        device.pressHome()
        val launchPackage: String = device.launcherPackageName
        assertFalse(launchPackage.isEmpty())

        device.wait(
            Until.hasObject(
                By.pkg(launchPackage).depth(0),
            ),
            LAUNCH_TIMEOUT,
        )

        val intent = context.packageManager.getLaunchIntentForPackage(appPackageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        context.startActivity(intent)
    }

    protected fun waitForText(@StringRes stringRes: Int, timeout: Long = DEFAULT_TIMEOUT) {
        val stringLiteral = context.getString(stringRes)
        waitForText(stringLiteral, timeout)
    }

    protected fun waitForText(string: String, timeout: Long = DEFAULT_TIMEOUT) {
        device.wait(Until.findObject(By.text(string)), timeout)
    }
}
