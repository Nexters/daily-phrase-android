package com.silvertown.android.dailyphrase.presentation.util

import android.content.ActivityNotFoundException
import android.content.Context
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.detail.Url
import timber.log.Timber

internal fun sendKakaoLink(
    context: Context,
    phraseId: Long,
    title: String,
    description: String,
    imageUrl: String,
    accessToken: String,
    likeCount: Int = 0,
    commentCount: Int = 0,
    sharedCount: Int = 0,
    viewCount: Int = 0,
    logShareEvent: () -> Unit,
) {
    val webUrl = Url.webUrl + phraseId

    val phraseFeed = FeedTemplate(
        content = Content(
            title = title,
            description = description,
            imageUrl = imageUrl,
            link = Link(
                webUrl = webUrl,
                mobileWebUrl = webUrl
            )
        ),
        social = Social(
            likeCount = likeCount,
            commentCount = commentCount,
            sharedCount = sharedCount,
            viewCount = viewCount
        ),
        buttons = listOf(
            Button(
                title = context.resources.getString(R.string.more_see),
                Link(
                    webUrl = webUrl,
                    mobileWebUrl = webUrl
                )
            )
        )
    )

    val serverCallbackArgs = mapOf(
        "accessToken" to accessToken
    )

    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        ShareClient.instance.shareDefault(
            context,
            phraseFeed,
            serverCallbackArgs
        ) { sharingResult, error ->
            if (error != null) {
                Timber.e(error)
                Timber.e("카카오톡 공유 실패", error)
            } else if (sharingResult != null) {
                Timber.d("카카오톡 공유 성공 ${sharingResult.intent}")
                context.startActivity(sharingResult.intent)

                Timber.w("Warning Msg: ${sharingResult.warningMsg}")
                Timber.w("Argument Msg: ${sharingResult.argumentMsg}")
                logShareEvent()
            }
        }
    } else {
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(phraseFeed, serverCallbackArgs)

        try {
            KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
        } catch (e: UnsupportedOperationException) {
            // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
        }

        try {
            KakaoCustomTabsClient.open(context, sharerUrl)
        } catch (e: ActivityNotFoundException) {
            // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
        }
    }
}