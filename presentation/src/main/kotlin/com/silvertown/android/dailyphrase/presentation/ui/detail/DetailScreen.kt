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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social
import com.silvertown.android.dailyphrase.presentation.BuildConfig
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.component.BaseWebView
import com.silvertown.android.dailyphrase.presentation.component.DailyPhraseBaseShell
import com.silvertown.android.dailyphrase.presentation.component.DetailBottomAction
import com.silvertown.android.dailyphrase.presentation.component.KakaoLoginDialog
import com.silvertown.android.dailyphrase.presentation.component.baseSnackbar
import com.silvertown.android.dailyphrase.presentation.util.ActionType
import com.silvertown.android.dailyphrase.presentation.util.sendKakaoLink
import com.silvertown.android.dailyphrase.presentation.util.vibrateSingle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

object Url {
    const val webUrl = "https://www.daily-phrase.com/phrase-web/"
    const val mobileWebUrl = "https://www.daily-phrase.com/phrase-webview/"
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
            logShareEvent = detailViewModel::logShareEvent,
            onClickShare = detailViewModel::onClickShare,
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
    logShareEvent: () -> Unit,
    onClickShare: () -> Unit,
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

        AdmobBanner(modifier = Modifier.fillMaxWidth())
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
                                    uiState = uiState,
                                    logShareEvent = logShareEvent
                                )
                            }
                        )
                    }
                }
            },
            onClickShare = {
                actionState.value = ActionType.SHARE.name
                onClickShare()
                if (uiState.isLoggedIn) {
                    sendKakaoLink(
                        context = context,
                        uiState = uiState,
                        logShareEvent = logShareEvent
                    )
                }
            }
        )
    }
}

private fun sendKakaoLink(
    context: Context,
    uiState: DetailUiState,
    logShareEvent: () -> Unit,
) {
    sendKakaoLink(
        context = context,
        phraseId = uiState.phraseId,
        title = uiState.title,
        description = uiState.content,
        imageUrl = uiState.imageUrl,
        likeCount = uiState.likeCount,
        commentCount = uiState.commentCount,
        sharedCount = uiState.sharedCount,
        viewCount = uiState.viewCount,
        logShareEvent = logShareEvent
    )
}

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val adSize = remember {
        val displayMetrics = context.resources.displayMetrics
        val widthPixels = displayMetrics.widthPixels.toFloat()
        val density = displayMetrics.density
        val adWidth = (widthPixels / density).toInt()
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = BuildConfig.BANNER_UNIT_ID
                loadAd(AdRequest.Builder().build())
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}
