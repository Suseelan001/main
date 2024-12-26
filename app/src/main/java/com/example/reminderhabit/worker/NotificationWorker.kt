package com.example.reminderhabit.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderhabit.R
import com.example.reminderhabit.view.BaseActivity

class NotificationWorkerClass(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString("title")
        val description = inputData.getString("description")

        if (title != null && description != null) {
            showNotification(title, description, 1, applicationContext)
        }

        return Result.success()
    }

    private fun showNotification(title: String, description: String, id: Int, context: Context) {
        val channelId = "REMINDER_CHANNEL"
        createNotificationChannel(context, channelId)

        val intent = Intent(context, BaseActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.profile) // Replace with your app's notification icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // Attach PendingIntent
            .setAutoCancel(true) // Dismiss notification when clicked

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, builder.build())
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Reminder Notifications"
            val channelDescription = "Channel for reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
