package com.example.reminderhabit.work

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.reminderhabit.R
import com.example.reminderhabit.view.BaseActivity
import java.util.Calendar


class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    private val notificationId = 17

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {




            val intent = Intent(applicationContext, BaseActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )


            val body = "Hello, It's time to water your  and spray pesticides to avoid powdery mildew."
            val builder = NotificationCompat.Builder(applicationContext, "BaseApplication.CHANNEL_ID")
                .setSmallIcon(R.drawable.profile)
                .setContentTitle("Reminder App.")
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(notificationId, builder.build())
            }


        return Result.success()
    }


    companion object {
        const val nameKey = "NAME"
    }
}