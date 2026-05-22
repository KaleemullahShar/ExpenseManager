package com.spendwise

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.spendwise.firebase.FirebaseInitializer
import com.spendwise.notifications.NotificationHelper
import com.spendwise.notifications.ReminderWorker
import com.spendwise.utils.PreferenceManager
import java.util.concurrent.TimeUnit

class SpendWiseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseInitializer.initialize(this)
        NotificationHelper.createChannels(this)
        AppCompatDelegate.setDefaultNightMode(PreferenceManager(this).themeMode)
        val reminder = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            reminder
        )
    }
}
