package com.silvertown.android.dailyphrase.messaging.component

import android.app.NotificationManager

interface MessagingManager {
    fun requestMessagingNotification(
        title: String,
        body: String,
        notificationManager: NotificationManager,
    )
}
