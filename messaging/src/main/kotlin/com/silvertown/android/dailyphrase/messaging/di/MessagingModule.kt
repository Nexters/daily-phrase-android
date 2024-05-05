package com.silvertown.android.dailyphrase.messaging.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.silvertown.android.dailyphrase.messaging.component.FirebaseTopicSubscriber
import com.silvertown.android.dailyphrase.messaging.component.MessagingManager
import com.silvertown.android.dailyphrase.messaging.component.MessagingManagerImpl
import com.silvertown.android.dailyphrase.messaging.component.MessagingSubscriber
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MessagingModule {

    @Binds
    abstract fun bindsMessagingManager(
        messagingManager: MessagingManagerImpl,
    ): MessagingManager

    @Binds
    abstract fun providesMessagingSubscriber(
        firebaseTopicSubscriber: FirebaseTopicSubscriber,
    ): MessagingSubscriber

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseMessaging(): FirebaseMessaging = Firebase.messaging
    }
}
