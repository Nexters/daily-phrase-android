package com.silvertown.android.dailyphrase.presentation.ui.detail

import android.app.Activity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.DailyPhraseBaseShell
import com.silvertown.android.dailyphrase.presentation.component.BaseWebView
import com.silvertown.android.dailyphrase.presentation.component.DetailBottomAction
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.component.KakaoLoginDialog
import com.silvertown.android.dailyphrase.presentation.component.baseSnackbar
import com.silvertown.android.dailyphrase.presentation.ui.ActionType
import com.silvertown.android.dailyphrase.presentation.util.vibrateSingle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

object Url {
    const val webUrl = "https://daily-phrase-web-web-kappa.vercel.app/phrase-webview/"
    const val mobileWebUrl = "https://daily-phrase-web-web-kappa.vercel.app/phrase-webview/"
}

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
    val context = LocalContext.current

    DailyPhraseBaseShell(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigateToBack = { navigateToBack() },
            )
        },
        snackbarHostState = snackbarHostState,
    ) {
        DetailBody(
            modifier = Modifier,
            context = context,
            snackbarScope = snackbarScope,
            snackbarHostState = snackbarHostState,
            uiState = detailUiState,
            onClickLike = detailViewModel::onClickLike,
            onClickBookmark = detailViewModel::onClickBookmark,
            showLoingDialog = detailViewModel::showLoginDialog,
        )
    }
}

@Composable
fun DetailBody(
    modifier: Modifier,
    context: Context,
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    uiState: DetailUiState,
    onClickLike: () -> Unit,
    onClickBookmark: () -> Unit,
    showLoingDialog: (Boolean) -> Unit,
) {
    val actionState = rememberSaveable {
        mutableStateOf(ActionType.NONE.name)
    }

    val messageRes = when (ActionType.valueOf(actionState.value)) {
        ActionType.LIKE -> R.string.login_and_like_message
        ActionType.BOOKMARK -> R.string.login_and_bookmark_message
        ActionType.SHARE -> R.string.login_and_share_message
        ActionType.NONE -> R.string.login_and_share_message
    }

    if (uiState.showLoginDialog) {
        BaseDialog(
            modifier = Modifier,
            onDismissRequest = { showLoingDialog(false) }
        ) {
            KakaoLoginDialog(
                message = messageRes,
                onClickKaKaoLogin = {
                    (context as? Activity as? MainActivity)?.kakaoLogin()
                },
                onDismissRequest = { showLoingDialog(false) }
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            BaseWebView(url = Url.mobileWebUrl + uiState.phraseId)
        }

        DetailBottomAction(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(60.dp),
            isLike = uiState.isLike,
            isBookmark = uiState.isBookmark,
            onClickLike = {
                actionState.value = ActionType.LIKE.name
                onClickLike()
                vibrateSingle(context)
            },
            onClickBookmark = {
                actionState.value = ActionType.BOOKMARK.name
                onClickBookmark()
                vibrateSingle(context)
                if (!uiState.isBookmark && uiState.isLoggedIn) {
                    snackbarScope.launch {
                        baseSnackbar(
                            snackbarHostState = snackbarHostState,
                            message = context.getString(R.string.bookmark_snackbar_message),
                            actionLabel = context.getString(R.string.bookmark_snackbar_action_label),
                            actionPerformed = {
                                sendKakaoLink(
                                    context = context,
                                    uiState = uiState
                                )
                            }
                        )
                    }
                }
            },
            onClickShare = {
                actionState.value = ActionType.SHARE.name
                sendKakaoLink(
                    context = context,
                    uiState = uiState,
                )
            }
        )
    }
}

private fun sendKakaoLink(
    context: Context,
    uiState: DetailUiState,
) {
    val webUrl = Url.webUrl + uiState.phraseId
    val mobileWebUrl = Url.mobileWebUrl + uiState.phraseId

    val phraseFeed = FeedTemplate(
        content = Content(
            title = uiState.title,
            description = uiState.content,
            imageUrl = uiState.imageUrl,
            link = Link(
                webUrl = webUrl,
                mobileWebUrl = mobileWebUrl
            )
        ),
        social = Social(
            likeCount = uiState.likeCount,
            commentCount = uiState.commentCount,
            sharedCount = uiState.sharedCount,
            viewCount = uiState.viewCount
        ),
        buttons = listOf(
            Button(
                title = context.resources.getString(R.string.more_see),
                Link(
                    webUrl = webUrl,
                    mobileWebUrl = mobileWebUrl
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
