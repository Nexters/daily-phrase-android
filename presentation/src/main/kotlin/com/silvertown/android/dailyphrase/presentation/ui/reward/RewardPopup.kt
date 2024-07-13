package com.silvertown.android.dailyphrase.presentation.ui.reward

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.util.EventTimer
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.BalloonWindow
import com.skydoves.balloon.compose.rememberBalloonBuilder

@Composable
internal fun RewardPopup(
    rewardBanner: RewardBanner,
    onTimeBelowThreshold: () -> Unit,
) {
    var balloonWindow: BalloonWindow? by remember { mutableStateOf(null) }

    val builder = rememberBalloonBuilder {
        setArrowSize(8)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setArrowPosition(0.5f)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPaddingLeft(12)
        setPaddingRight(8)
        setPaddingVertical(8)
        setCornerRadius(4f)
        setBackgroundColorResource(R.color.tooltip_background)
        setBalloonAnimation(BalloonAnimation.ELASTIC)
        setDismissWhenClicked(true)
        setDismissWhenTouchOutside(false)
        //setAutoDismissDuration(2000)
    }

    val annotatedText = buildAnnotatedString {
        append(stringResource(id = R.string.reward_popup_text_prefix))
        withStyle(style = SpanStyle(color = colorResource(id = R.color.orange))) {
            append(" " + rewardBanner.shortName + " ")
        }
        append(stringResource(id = R.string.reward_popup_text_suffix))
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Balloon(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp), // 하단부터 팝업포지션 offset
            builder = builder,
            onBalloonWindowInitialized = { balloonWindow = it },
            onComposedAnchor = { balloonWindow?.showAlignTop() },
            balloonContent = {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EventTimer(
                        eventEndedTime = rewardBanner.eventEndDateTime,
                        onTimeBelowThreshold = onTimeBelowThreshold
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_fill),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
        ) { balloonWindow ->
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.orange),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(id = R.color.white))
                        .padding(
                            start = 16.dp,
                            end = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = annotatedText,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = pretendardFamily,
                            fontWeight = FontWeight.SemiBold
                        ), color = colorResource(id = R.color.black)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_ticket_30),
                        contentDescription = null
                    )
                }

                Box(
                    modifier = Modifier
                        .offset(x = 3.dp, y = (-1).dp) // count 포지션 offset
                        .widthIn(min = 18.dp)
                        .height(18.dp)
                        .background(
                            color = colorResource(id = R.color.bright_red),
                            shape = CircleShape
                        )
                        .padding(horizontal = 4.5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = rewardBanner.totalEntryCount.toString(),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendardFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = colorResource(id = R.color.white),
                    )
                }
            }
        }
    }
}
