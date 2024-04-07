package com.silvertown.android.dailyphrase.messaging.component

import android.app.NotificationManager
import android.content.Context
import com.silvertown.android.dailyphrase.messaging.initializers.messagingNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MessagingManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MessagingManager {
    override fun requestMessagingNotification(
        title: String,
        body: String,
        notificationManager: NotificationManager,
    ) {
        val builder = context
            .messagingNotification()
            .setContentTitle(title)
            .setContentText(body)
            .build()

        notificationManager.notify(0, builder)
    }
}
