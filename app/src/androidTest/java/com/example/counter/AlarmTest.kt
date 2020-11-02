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
import androidx.test.uiautomator.*
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


const val ALARM_PKG = "com.google.android.deskclock"

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class AlarmTest {

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
        i.component = ComponentName(ALARM_PKG, "com.android.deskclock.DeskClock")
        i.action = "android.intent.action.MAIN"
        i.addCategory("android.intent.category.LAUNCHER")
        i.addCategory("android.intent.category.DEFAULT")
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)

        // Wait for the app to appear
        device.wait(
                Until.hasObject(By.pkg(CONTACTS_PKG).depth(0)),
                5000
        )
    }

    @Test
    fun addAlarm() {
        val alarmTab = device.findObject(By.text("Alarm"))
        alarmTab.click()

        device.wait(Until.hasObject(By.res(ALARM_PKG, "fab")), 5000)
        val fab = device.findObject(By.res(ALARM_PKG, "fab"))
        fab.click()

        val okButton = device.wait(Until.findObject(By.text("OK")), 5000)
        okButton.click()

        device.wait(
                Until.findObject(
                        By.clazz("android.support.v7.widget.RecyclerView")
                                .hasChild(By.res(ALARM_PKG, "onoff"))), 5000)

        Thread.sleep(1000)

        val list = UiScrollable(UiSelector().className("android.support.v7.widget.RecyclerView"))
        val switch = list.getChildByInstance(UiSelector().resourceId("com.google.android.deskclock:id/onoff"), 2)

        assertEquals(switch.text, "ON")
    }
}