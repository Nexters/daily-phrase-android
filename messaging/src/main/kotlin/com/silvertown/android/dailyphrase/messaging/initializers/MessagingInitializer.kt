package com.silvertown.android.dailyphrase.messaging.initializers

import com.silvertown.android.dailyphrase.messaging.component.MessagingSubscriber
import javax.inject.Inject

class MessagingInitializer @Inject constructor(
    private val messagingSubscriber: MessagingSubscriber,
) {
    fun initialize() {
        messagingSubscriber.subscribe()
    }
}
