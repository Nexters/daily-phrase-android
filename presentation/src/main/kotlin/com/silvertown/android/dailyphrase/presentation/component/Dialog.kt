package com.silvertown.android.dailyphrase.presentation.component

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.silvertown.android.dailyphrase.domain.model.PrizeInfo
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    @ColorRes backgroundColor: Int = R.color.white,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            modifier = modifier,
            cornerRadius = 8.dp,
            backgroundColor = backgroundColor
        ) {
            content()
        }
    }
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    cornerRadius: Dp,
    backgroundColor: Int,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(cornerRadius),
            color = colorResource(id = backgroundColor),
        ) {
            content()
        }
    }
}

@Composable
fun KakaoLoginDialog(
    @StringRes message: Int,
    onClickKaKaoLogin: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(max = 270.dp)
            .padding(
                vertical = 20.dp,
                horizontal = 15.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = message),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        KaKaoLoginButton(
            modifier = Modifier.fillMaxWidth(),
            onClickKaKaoLogin = onClickKaKaoLogin,
        )

        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
                horizontal = 40.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.close),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.Normal
                ),
                color = colorResource(id = R.color.black)
            )
        }
    }
}

@Composable
fun UnderConstructionDialog(
    modifier: Modifier = Modifier,
    @StringRes message: Int = R.string.feature_under_construction,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = modifier
            .widthIn(max = 270.dp)
            .padding(
                vertical = 20.dp,
                horizontal = 15.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = message),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))


        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.orange),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
            )
        ) {
            Text(
                text = stringResource(id = R.string.close),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(id = R.color.white)
            )
        }
    }
}

@Composable
fun LogoutDialog(
    modifier: Modifier = Modifier,
    onClickLogout: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(270.dp)
            .padding(
                top = 32.dp,
                bottom = 20.dp,
                start = 20.dp,
                end = 20.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.confirm_logout),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.orange),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
            )
        ) {
            Text(
                text = stringResource(id = R.string.keep_logged_in),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(id = R.color.white)
            )
        }
        Button(
            onClick = { onClickLogout() },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
                horizontal = 40.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.Normal
                ),
                color = colorResource(id = R.color.black)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeEventModal(
    prizeInfo: List<PrizeInfo.Item>,
    pagerState: PagerState,
    onClickKaKaoLogin: () -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val pagerItemSize = 140.dp
    val pagerHorizontalPadding = 22.dp
    val pagerContentPadding =
        (screenWidth - (pagerHorizontalPadding * 2) - pagerItemSize) / 2

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(colorResource(id = R.color.event_bg)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        end = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = modifier.size(28.dp),
                    onClick = onDismissRequest
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_28),
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = stringResource(id = R.string.welcome_event_model_login_and_get_prize),
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(id = R.color.black)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    2.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile_event),
                    tint = colorResource(id = R.color.orange),
                    contentDescription = null
                )
                Text(
                    text = stringResource(
                        id = R.string.welcome_event_model_participation_count,
                        prizeInfo.firstOrNull()?.totalParticipantCount ?: 0
                    ),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = pretendardFamily,
                        fontWeight = FontWeight.Medium
                    ),
                    color = colorResource(id = R.color.orange)
                )
            }
            Spacer(modifier = Modifier.height(35.dp))

            HorizontalPager(
                modifier = Modifier.fillMaxWidth(),
                state = pagerState,
                userScrollEnabled = false,
                contentPadding = PaddingValues(horizontal = pagerContentPadding),
                pageSpacing = 16.dp,
            ) { page ->
                val actualPage = page % prizeInfo.size
                val prize = prizeInfo[actualPage]

                Box(
                    modifier = Modifier
                        .size(pagerItemSize)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colorResource(id = R.color.white)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = prize.welcomeImageUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(84.dp))

            KaKaoLoginButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                title = R.string.simple_login,
                onClickKaKaoLogin = onClickKaKaoLogin,
            )
            Spacer(modifier = Modifier.height(40.dp))

        }
    }
}

class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.item_loading)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
