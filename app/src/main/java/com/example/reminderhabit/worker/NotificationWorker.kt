package com.example.reminderhabit.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderhabit.R
import java.util.Calendar

class NotificationWorkerClass(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {


        val title = inputData.getString("title")

        if (title != null) {
            showNotification(title, "", 1, applicationContext)
        }


        return Result.success()
    }

    private fun showNotification(title: String, description: String, id: Int, context: Context) {
        val channelId = "REMINDER_CHANNEL"
        createNotificationChannel(context, channelId)

        // Build and show the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.profile) // Replace with your app's notification icon
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

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
