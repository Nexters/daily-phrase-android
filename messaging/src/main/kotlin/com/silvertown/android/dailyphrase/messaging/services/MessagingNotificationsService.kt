package com.silvertown.android.dailyphrase.messaging.services

import android.app.NotificationManager
import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.silvertown.android.dailyphrase.messaging.component.MessagingManager
import com.silvertown.android.dailyphrase.messaging.initializers.TOPIC_SENDER
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MessagingNotificationsService : FirebaseMessagingService() {

    @Inject
    lateinit var messagingManager: MessagingManager

    override fun onMessageReceived(message: RemoteMessage) {
        if (TOPIC_SENDER == message.from) {
            message.notification?.let { notification ->
                val notificationManager: NotificationManager? =
                    getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

                requestMessagingNotification(notificationManager, notification)
            }
        }
    }

    private fun requestMessagingNotification(
        notificationManager: NotificationManager?,
        notification: RemoteMessage.Notification,
    ) {
        notificationManager?.let {
            messagingManager.requestMessagingNotification(
                title = notification.title.orEmpty(),
                body = notification.body.orEmpty(),
                notificationManager = notificationManager
            )
        }
    }
}
