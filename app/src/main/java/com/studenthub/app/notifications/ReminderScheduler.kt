package com.studenthub.app.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

/** One entry point for every module: ReminderScheduler.schedule(...) / cancel(...) */
object ReminderScheduler {
    private const val ONE_DAY = 24L * 60 * 60 * 1000

    /** Reminds 24h before [eventTime] (or in ~10s if that moment already passed). */
    fun schedule(context: Context, key: String, title: String, message: String, eventTime: Long) {
        val now = System.currentTimeMillis()
        if (eventTime <= now) return
        val fireAt = maxOf(eventTime - ONE_DAY, now + 10_000)
        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(fireAt - now, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("title" to title, "message" to message, "id" to key.hashCode()))
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork(key, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancel(context: Context, key: String) {
        WorkManager.getInstance(context).cancelUniqueWork(key)
    }
}
