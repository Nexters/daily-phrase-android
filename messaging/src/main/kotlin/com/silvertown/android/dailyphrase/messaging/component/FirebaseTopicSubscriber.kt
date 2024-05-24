package com.silvertown.android.dailyphrase.messaging.component

import com.google.firebase.messaging.FirebaseMessaging
import com.silvertown.android.dailyphrase.messaging.initializers.TEST_TOPIC_SENDER
import com.silvertown.android.dailyphrase.messaging.initializers.TOPIC_SENDER
import com.silvertown.android.dailyphrase.presentation.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class FirebaseTopicSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : MessagingSubscriber {

    override fun subscribe() {
        firebaseMessaging
            .subscribeToTopic(
                if (BuildConfig.DEBUG) TEST_TOPIC_SENDER else TOPIC_SENDER
            )
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.d("Failed to get topic message")
                }
            }
    }
}
