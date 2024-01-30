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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.silvertown.android.dailyphrase.presentation.component.BaseWebView
import com.silvertown.android.dailyphrase.presentation.component.DetailBottomAction
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.component.baseSnackbar
import com.silvertown.android.dailyphrase.presentation.util.vibrateSingle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    modifier: Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
) {
    val detailUiState by detailViewModel.detailUiState.collectAsStateWithLifecycle()
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
            uiState = detailUiState,
            onClickLike = detailViewModel::onClickLike,
            onClickBookmark = detailViewModel::onClickBookmark,
        )
    }
}

@Composable
fun DetailBody(
    modifier: Modifier,
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    uiState: DetailUiState,
    onClickLike: () -> Unit,
    onClickBookmark: () -> Unit,
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
            isLike = uiState.isLike,
            isBookmark = uiState.isBookmark,
            onClickLike = {
                onClickLike()
                vibrateSingle(context)
            },
            onClickBookmark = {
                onClickBookmark()
                vibrateSingle(context)
                if (!uiState.isBookmark) {
                    snackbarScope.launch {
                        baseSnackbar(
                            snackbarHostState = snackbarHostState,
                            message = context.getString(R.string.bookmark_snackbar_message),
                            actionLabel = context.getString(R.string.bookmark_snackbar_action_label),
                            actionPerformed = {}
                        )
                    }
                }
            },
            onClickShare = {
                sendKakaoLink(
                    context = context,
                    title = uiState.title,
                    description = uiState.content,
                    imageUrl = uiState.imageUrl,
                    phraseId = uiState.phraseId,
                    likeCount = uiState.likeCount,
                    commentCount = uiState.commentCount,
                    sharedCount = uiState.sharedCount,
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
    phraseId: Long,
    likeCount: Int? = null,
    commentCount: Int? = null,
    sharedCount: Int? = null,
    viewCount: Int? = null,
) {
    /**
     * TODO: Url 수정 예정
     */
    val phraseFeed = FeedTemplate(
        content = Content(
            title = title,
            description = description,
            imageUrl = imageUrl,
            link = Link(
                webUrl = "https://www.naver.com/${phraseId}",
                mobileWebUrl = "https://www.naver.com/${phraseId}"
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
                    webUrl = "https://www.naver.com/${phraseId}",
                    mobileWebUrl = "https://www.naver.com/${phraseId}"
                )
            )
        )
    )

    if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
        ShareClient.instance.shareDefault(context, phraseFeed) { sharingResult, error ->
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
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(phraseFeed)

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


