package com.silvertown.android.dailyphrase.messaging.component

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.silvertown.android.dailyphrase.messaging.initializers.messagingNotification
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.util.Constants.PHRASE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

class MessagingManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : MessagingManager {

    override fun requestMessagingNotification(
        title: String,
        body: String,
        notificationManager: NotificationManager,
        phraseId: String?,
    ) {
        val pendingIntent = handlePendingIntent(phraseId)

        val builder = context
            .messagingNotification(pendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .build()

        notificationManager.notify(0, builder)
    }

    private fun handlePendingIntent(phraseId: String?): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(PHRASE_ID, phraseId)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        return PendingIntent.getActivity(
            context,
            UUID.randomUUID().hashCode(),
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
