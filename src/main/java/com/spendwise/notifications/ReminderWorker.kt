package com.spendwise.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        NotificationHelper.notify(applicationContext, 101, "Track today's spending", "Log new expenses while they are fresh.")
        return Result.success()
    }

    companion object { const val WORK_NAME = "daily_expense_reminder" }
}
