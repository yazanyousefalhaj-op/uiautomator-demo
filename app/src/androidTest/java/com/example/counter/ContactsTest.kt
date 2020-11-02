package com.example.counter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.startActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


const val CONTACTS_PKG = "com.android.contacts"

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class ContactsTest {

    private lateinit var device: UiDevice

    @Before
    fun startContactsApp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, CoreMatchers.notNullValue())
        device.wait(
                Until.hasObject(By.pkg(launcherPackage).depth(0)),
                5000
        )

        val context = ApplicationProvider.getApplicationContext<Context>()

        // Launch the app
        val i = Intent()
        i.component = ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity")
        i.action = "android.intent.action.MAIN"
        i.addCategory("android.intent.category.LAUNCHER")
        i.addCategory("android.intent.category.DEFAULT")
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)

        // Wait for the app to appear
        device.wait(
                Until.hasObject(By.pkg(CONTACTS_PKG).depth(0)),
                5000
        )
    }

    @Test
    fun addContact() {
        val fab = device.findObject(By.res(CONTACTS_PKG, "floating_action_button"))
        fab.click()

        device.wait(Until.findObject(By.text("First name")), 5000)
        val firstNameInput = device.findObject(By.text("First name"))
        firstNameInput.text = "John"

        val lastNameInput = device.findObject(By.text("Last name"))
        lastNameInput.text = "Doe"

        val phoneInput = device.findObject(By.text("Phone"))
        phoneInput.text = "5555555555"

        val saveButton = device.findObject(By.res(CONTACTS_PKG, "editor_menu_save_button"))
        saveButton.click()

        device.wait(Until.findObject(By.res(CONTACTS_PKG, "large_title")), 5000)
        val nameText = device.findObject(By.res(CONTACTS_PKG, "large_title"))
        assertEquals(nameText.text, "John Doe")

        device.pressBack()

        device.wait(Until.findObject(By.res(CONTACTS_PKG, "cliv_name_textview")), 5000)
        val listNameText = device.findObject(By.res(CONTACTS_PKG, "cliv_name_textview"))
        assertEquals(listNameText.text, "John Doe")
    }
}