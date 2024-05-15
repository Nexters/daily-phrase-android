package com.silvertown.android.dailyphrase.messaging.initializers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.silvertown.android.dailyphrase.messaging.R

const val TOPIC_SENDER = "/topics/update_phrase"
const val TEST_TOPIC_SENDER = "/topics/test"
private const val MESSAGING_NOTIFICATION_CHANNEL_ID = "MessagingNotificationChannel"

fun Context.messagingNotification(
    pendingIntent: PendingIntent? = null,
): NotificationCompat.Builder {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            MESSAGING_NOTIFICATION_CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description =
                getString(R.string.notification_channel_description)
        }

        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        notificationManager?.createNotificationChannel(channel)
    }

    return NotificationCompat.Builder(
        this,
        MESSAGING_NOTIFICATION_CHANNEL_ID,
    )
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.push_icon)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
}
