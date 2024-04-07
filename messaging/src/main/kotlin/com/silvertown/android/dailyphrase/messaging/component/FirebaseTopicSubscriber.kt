package com.silvertown.android.dailyphrase.messaging.component

import com.google.firebase.messaging.FirebaseMessaging
import com.silvertown.android.dailyphrase.messaging.initializers.TOPIC_SENDER
import timber.log.Timber
import javax.inject.Inject

class FirebaseTopicSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : MessagingSubscriber {
    override fun subscribe() {
        firebaseMessaging
            .subscribeToTopic(TOPIC_SENDER)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.d("Failed to get topic message")
                }
            }
    }
}
