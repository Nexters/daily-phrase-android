package com.silvertown.android.dailyphrase.presentation.ui.mypage.unsubscribe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.base.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.ui.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.ui.component.ItemDivider

@Composable
fun UnsubscribeScreen(
    modifier: Modifier = Modifier,
    navigationToBack: () -> Unit,
) {
    Content(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white)),
        userName = "김춘배",
        navigationToBack = navigationToBack,
        onClickUnsubscribed = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    userName: String,
    navigationToBack: () -> Unit,
    onClickUnsubscribed: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                navigationContent = {
                    IconButton(onClick = { navigationToBack() }) {
                        Icon(
                            painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
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
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            UnsubscribeBody(
                modifier = Modifier,
                userName = userName + stringResource(id = R.string.user_name_suffix),
                onClickUnsubscribed = onClickUnsubscribed
            )
        }
    }
}

@Composable
private fun UnsubscribeBody(
    modifier: Modifier = Modifier,
    userName: String,
    onClickUnsubscribed: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = userName + stringResource(id = R.string.message_before_leaving),
            style = TextStyle(
                fontSize = 28.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.black),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))
        InfoMessageBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
            color = colorResource(id = R.color.orange),
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
            iconId = R.drawable.ic_bookmark_60,
            titleId = R.string.unsubscribe_message_title_1,
            subtitleId = R.string.unsubscribe_message_subtitle_1
        )

        Spacer(modifier = Modifier.height(40.dp))
        ItemDivider()
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
