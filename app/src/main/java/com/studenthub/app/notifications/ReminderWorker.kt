package com.studenthub.app.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.studenthub.app.MainActivity

class ReminderWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val ctx = applicationContext
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val manager = ctx.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "Reminders", NotificationManager.IMPORTANCE_HIGH)
        )
        val openApp = PendingIntent.getActivity(
            ctx, 0, Intent(ctx, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(inputData.getString("title") ?: "Reminder")
            .setContentText(inputData.getString("message") ?: "")
            .setContentIntent(openApp)
            .setAutoCancel(true)
            .build()
        manager.notify(inputData.getInt("id", 0), notification)
        return Result.success()
    }

    companion object { const val CHANNEL_ID = "student_hub_reminders" }
}
