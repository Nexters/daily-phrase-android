package com.silvertown.android.dailyphrase.messaging.services

import android.app.NotificationManager
import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.silvertown.android.dailyphrase.messaging.component.MessagingManager
import com.silvertown.android.dailyphrase.messaging.initializers.TEST_TOPIC_SENDER
import com.silvertown.android.dailyphrase.messaging.initializers.TOPIC_SENDER
import com.silvertown.android.dailyphrase.presentation.BuildConfig
import com.silvertown.android.dailyphrase.presentation.util.Constants.PHRASE_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MessagingNotificationsService : FirebaseMessagingService() {

    @Inject
    lateinit var messagingManager: MessagingManager

    override fun onMessageReceived(message: RemoteMessage) {
        if (TOPIC_MESSAGE_SENDER == message.from) {
            message.notification?.let { notification ->
                val notificationManager: NotificationManager? =
                    getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

                val phraseId = message.data[PHRASE_ID]

                requestMessagingNotification(
                    notificationManager,
                    notification,
                    phraseId
                )
            }
        }
    }

    private fun requestMessagingNotification(
        notificationManager: NotificationManager?,
        notification: RemoteMessage.Notification,
        phraseId: String?,
    ) {
        val title = notification.title

        if (title.isNullOrEmpty()) return

        notificationManager?.let {
            messagingManager.requestMessagingNotification(
                title = title,
                body = notification.body.orEmpty(),
                notificationManager = notificationManager,
                phraseId = phraseId
            )
        }
    }

    companion object {
        private val TOPIC_MESSAGE_SENDER =
            if (BuildConfig.DEBUG) TEST_TOPIC_SENDER else TOPIC_SENDER
    }
}
