package com.example.counter

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


const val PKG_NAME = "com.example.counter"

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainActivityTest {

    private lateinit var device: UiDevice

    @Before
    fun startMainActivity() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
                Until.hasObject(By.pkg(launcherPackage).depth(0)),
                5000
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
                "com.example.counter")?.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
                Until.hasObject(By.pkg("com.example.counter").depth(0)),
                5000
        )
    }

    @Test
    fun mainActivity_loginSuccess() {
        login("quality+global@equityzen.com",  "ezmoney1")

        val statusText = device.findObject(By.res(PKG_NAME, "status_text"))
        assertEquals("User is logged in", statusText.text)
    }

    @Test
    fun mainActivity_loginFailed() {
        login("quality+global@equityzen.com",  "ezmoney2")

        val statusText = device.findObject(By.res(PKG_NAME, "status_text"))
        assertEquals("Username or password is incorrect", statusText.text)
    }

    private fun login(email: String, password: String) {
        val emailInput = device.findObject(By.res(PKG_NAME, "email_input"))
        val passwordInput = device.findObject(By.res(PKG_NAME, "password_input"))

        emailInput.text = email
        passwordInput.text = password

        val button = device.findObject(UiSelector().className("android.widget.Button"))
        button.click()
    }
}