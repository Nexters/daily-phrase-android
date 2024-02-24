package com.silvertown.android.dailyphrase.presentation.ui.mypage.unsubscribe

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.component.DailyPhraseBaseShell
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.component.ItemDivider
import com.silvertown.android.dailyphrase.presentation.component.baseSnackbar
import com.silvertown.android.dailyphrase.presentation.extensions.navigateToStart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun UnsubscribeScreen(
    modifier: Modifier = Modifier,
    unsubscribeViewModel: UnsubscribeViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
) {
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Content(
        modifier = modifier
            .fillMaxSize(),
        snackbarScope = snackbarScope,
        snackbarHostState = snackbarHostState,
        navigateToBack = navigateToBack,
        isDeletedEvent = unsubscribeViewModel.isDeleted,
        getName = unsubscribeViewModel::getName,
        onClickUnsubscribed = unsubscribeViewModel::deleteMember
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    navigateToBack: () -> Unit,
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    isDeletedEvent: SharedFlow<Boolean>,
    getName: () -> String,
    onClickUnsubscribed: () -> Unit,
) {
    DailyPhraseBaseShell(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigateToBack = { navigateToBack() },
                titleContent = {
                    Text(
                        text = stringResource(id = R.string.unsubscribed_title),
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = pretendardFamily,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                },
            )
        },
        snackbarHostState = snackbarHostState
    ) {
        UnsubscribeBody(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            userName = getName() + stringResource(id = R.string.user_name_suffix),
            snackbarScope = snackbarScope,
            snackbarHostState = snackbarHostState,
            isDeletedEvent = isDeletedEvent,
            onClickUnsubscribed = onClickUnsubscribed
        )
    }
}

@Composable
private fun UnsubscribeBody(
    modifier: Modifier = Modifier,
    userName: String,
    onClickUnsubscribed: () -> Unit,
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    isDeletedEvent: SharedFlow<Boolean>,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        isDeletedEvent.collectLatest { isDeleted ->
            if (isDeleted) {
                context.navigateToStart(R.string.success_delete_member)
            } else {
                showSnackbar(snackbarScope, snackbarHostState, context)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 160.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = userName + stringResource(id = R.string.message_before_leaving),
            style = TextStyle(
                fontSize = 28.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.black),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(52.dp))

        InfoMessageBox(
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(80.dp))

        Text(
            modifier = Modifier
                .clickable { onClickUnsubscribed() },
            text = stringResource(id = R.string.confirm_unsubscribe_service),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.orange_text),
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
fun InfoMessageBox(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoMessageSection(
            iconId = R.drawable.ic_bookmark_fill_60,
            titleId = R.string.unsubscribe_message_title_1,
            subtitleId = R.string.unsubscribe_message_subtitle_1
        )
        Spacer(modifier = Modifier.height(40.dp))
        InfoMessageSection(
            iconId = R.drawable.ic_sad_60,
            titleId = R.string.unsubscribe_message_title_2,
            subtitleId = R.string.unsubscribe_message_subtitle_2
        )
    }
}

@Composable
fun InfoMessageSection(
    iconId: Int,
    titleId: Int,
    subtitleId: Int,
) {
    Icon(
        painter = painterResource(id = iconId),
        tint = colorResource(id = R.color.orange),
        contentDescription = null
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = stringResource(id = titleId),
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = pretendardFamily,
            fontWeight = FontWeight.Medium
        ),
        color = colorResource(id = R.color.black),
        textAlign = TextAlign.Center
    )
    Text(
        text = stringResource(id = subtitleId),
        style = TextStyle(
            fontSize = 16.sp,
            fontFamily = pretendardFamily,
            fontWeight = FontWeight.Normal
        ),
        color = colorResource(id = R.color.gray),
        textAlign = TextAlign.Center
    )
}

private suspend fun showSnackbar(
    snackbarScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    snackbarScope.launch {
        baseSnackbar(
            snackbarHostState = snackbarHostState,
            message = context.getString(R.string.failure_request),
            actionLabel = context.getString(R.string.confirm),
            actionPerformed = {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        )
    }
}
