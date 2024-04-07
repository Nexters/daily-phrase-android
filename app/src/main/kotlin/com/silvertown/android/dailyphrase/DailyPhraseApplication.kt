package com.silvertown.android.dailyphrase

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.kakao.sdk.common.KakaoSdk
import com.silvertown.android.dailyphrase.messaging.initializers.MessagingInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DailyPhraseApplication : Application() {

    @Inject
    lateinit var messagingInitializer: MessagingInitializer

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        initKakaoSdk()
        initAdmobSetting()
        messagingInitializer.initialize()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(
            context = this,
            appKey = BuildConfig.KAKAO_APP_KEY,
        )
    }

    private fun initAdmobSetting() {
        MobileAds.initialize(this) {}
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().build()
        )
    }
}
