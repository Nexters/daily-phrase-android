package com.silvertown.android.dailyphrase

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
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
    lateinit var flipperNetworkPlugin: NetworkFlipperPlugin

    @Inject
    lateinit var messagingInitializer: MessagingInitializer

    override fun onCreate() {
        super.onCreate()

        initKakaoSdk()
        initAdmobSetting()
        messagingInitializer.initialize()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            setDebugTool()
        }
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

    private fun setDebugTool() {
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(
                InspectorFlipperPlugin(
                    applicationContext,
                    DescriptorMapping.withDefaults(),
                ),
            )
            client.addPlugin(SharedPreferencesFlipperPlugin(this, "member_preferences.pb"))
            client.addPlugin(flipperNetworkPlugin)
            client.start()
        }
    }
}
