package com.silvertown.android.dailyphrase.presentation.ui.detail

import android.content.ActivityNotFoundException
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.component.DailyPhraseBaseShell
import com.silvertown.android.dailyphrase.presentation.component.baseSnackbar
import com.silvertown.android.dailyphrase.presentation.component.BaseWebView
import com.silvertown.android.dailyphrase.presentation.component.DetailBottomAction
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.util.vibrateSingle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier,
    postViewModel: DetailViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
) {
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    DailyPhraseBaseShell(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigateToBack = { navigateToBack() },
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        DetailBody(
            modifier = Modifier,
            snackbarScope = snackbarScope,
            snackbarHostState = snackbarHostState,
        )
    }
}

@Composable
fun DetailBody(
    modifier: Modifier,
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            BaseWebView(url = "https://m.blog.naver.com/woo762658/221790330636")
        }

        DetailBottomAction(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(60.dp),
            onFavoriteClick = {
                vibrateSingle(context)
            },
            onBookmarkClick = {
                snackbarScope.launch {
                    baseSnackbar(
                        snackbarHostState = snackbarHostState,
                        message = context.getString(R.string.bookmark_snackbar_message),
                        actionLabel = context.getString(R.string.bookmark_snackbar_action_label),
                    )
                }
                vibrateSingle(context)
            },
            onShareClick = {
                sendKakaoLink(
                    context = context,
                    title = "타이틀타이틀타이틀",
                    description = "가나다라마바사아자차카타파하가나다라마바사아자차카타파하",
                    imageUrl = "https://cdn.pixabay.com/photo/2015/06/25/04/50/hand-print-820913_1280.jpg",
                    likeCount = 332,
                    commentCount = 123,
                    sharedCount = 32,
                    postId = 1
                )
            }
        )
    }
}

private fun sendKakaoLink(
    context: Context,
    title: String,
    description: String,
    imageUrl: String,
    postId: Int,
    likeCount: Int? = null,
    commentCount: Int? = null,
    sharedCount: Int? = null,
) {
    val postFeed = FeedTemplate(
        content = Content(
            title = title,
            description = description,
            imageUrl = imageUrl,
            link = Link(
                webUrl = "https://www.naver.com",
                mobileWebUrl = "https://www.naver.com"
            )
        ),
        social = Social(
            likeCount = likeCount,
            commentCount = commentCount,
            sharedCount = sharedCount
        ),
        buttons = listOf(
            Button(
                title = context.resources.getString(R.string.more_see),
                Link(
                    webUrl = "https://www.naver.com",
                    mobileWebUrl = "https://www.naver.com"
                )
            )
        )
    )

    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        ShareClient.instance.shareDefault(context, postFeed) { sharingResult, error ->
            if (error != null) {
                Timber.e(error)
                Timber.e("카카오톡 공유 실패", error)
            } else if (sharingResult != null) {
                Timber.d("카카오톡 공유 성공 ${sharingResult.intent}")
                context.startActivity(sharingResult.intent)

                Timber.w("Warning Msg: ${sharingResult.warningMsg}")
                Timber.w("Argument Msg: ${sharingResult.argumentMsg}")
            }
        }
    } else {
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(postFeed)

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


