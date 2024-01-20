package com.silvertown.android.dailyphrase

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DailyPhraseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        initKakaoSdk()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(
            context = this,
            appKey = BuildConfig.KAKAO_APP_KEY,
        )
    }
}
