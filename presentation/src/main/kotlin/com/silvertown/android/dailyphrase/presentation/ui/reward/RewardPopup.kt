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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.domain.model.HomeRewardState
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.util.EventTimer
import com.silvertown.android.dailyphrase.presentation.util.calculateAcquirableTicketResetTime
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.BalloonWindow
import com.skydoves.balloon.compose.rememberBalloonBuilder
import kotlinx.coroutines.delay

@Composable
internal fun RewardPopup(
    state: HomeRewardState,
    showSharedEventTooltip: Boolean,
    showEndedEventTimerPopupTooltip: Boolean,
    onTimeBelowThreshold: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var acquirableTicketResetTimer by remember { mutableStateOf(calculateAcquirableTicketResetTime()) }
    val shouldRunAcquirableTicketResetTimer by remember {
        derivedStateOf { shouldRunAcquirableTicketResetTimer(state.shareCount) }
    }

    var balloonWindow: BalloonWindow? by remember { mutableStateOf(null) }

    val builder = rememberBalloonBuilder {
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setArrowPosition(0.5f)
        setArrowTopPadding(3)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPaddingLeft(12)
        setPaddingRight(8)
        setPaddingVertical(8)
        setCornerRadius(4f)
        setBackgroundColorResource(R.color.tooltip_background)
        setBalloonAnimation(BalloonAnimation.NONE)
        setDismissWhenClicked(true)
        setDismissWhenTouchOutside(false)
    }

    LaunchedEffect(shouldRunAcquirableTicketResetTimer) {
        if (shouldRunAcquirableTicketResetTimer) {
            while (true) {
                acquirableTicketResetTimer = calculateAcquirableTicketResetTime()
                delay(1000L)
            }
        }
    }

    val popupText = getPopupText(
        state = state,
        acquirableTicketResetTimer = acquirableTicketResetTimer
    )

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Balloon(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp), // 하단부터 팝업포지션 offset
            builder = builder,
            key = showSharedEventTooltip to showEndedEventTimerPopupTooltip,
            onBalloonWindowInitialized = { balloonWindow = it },
            balloonContent = {
                if (showSharedEventTooltip) {
                    TicketReceivedText()
                } else {
                    CountdownTimer(
                        state = state,
                        onTimeBelowThreshold = onTimeBelowThreshold
                    )
                }
            }
        ) { balloonWindow ->
            LaunchedEffect(showSharedEventTooltip, showEndedEventTimerPopupTooltip) {
                if (showSharedEventTooltip || showEndedEventTimerPopupTooltip) {
                    balloonWindow.showAlignTop()
                } else {
                    balloonWindow.dismiss()
                }
            }

            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                PopupContainer(popupText)

                if (state.rewardBanner.myTicketCount > 0) {
                    OwnedTicketBadge(
                        myTicketCount = state.rewardBanner.myTicketCount.toString()
                    )
                }
            }
        }
    }
}

@Composable
private fun TicketReceivedText() {
    Text(
        text = stringResource(id = R.string.get_ticket_title),
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = pretendardFamily,
            fontWeight = FontWeight.Medium
        ),
        color = colorResource(id = R.color.white)
    )
}

@Composable
private fun CountdownTimer(
    state: HomeRewardState,
    onTimeBelowThreshold: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        EventTimer(
            eventEndedTime = state.eventEndDateTime,
            onTimeBelowThreshold = onTimeBelowThreshold
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_close_fill),
            tint = Color.Unspecified,
            contentDescription = null
        )
    }
}

@Composable
private fun PopupContainer(
    popupText: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
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
            text = popupText,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = colorResource(id = R.color.black)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_ticket_30),
            contentDescription = null
        )
    }
}

@Composable
private fun OwnedTicketBadge(
    myTicketCount: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
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
            text = myTicketCount,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.white),
        )
    }
}

@Composable
private fun getPopupText(
    state: HomeRewardState,
    acquirableTicketResetTimer: String,
): AnnotatedString {
    val annotatedText = buildAnnotatedString {
        val orangeStyle = SpanStyle(color = colorResource(id = R.color.orange))
        val pretendardFamilyStyle = SpanStyle(fontFamily = pretendardFamily)
        val orangePretendardStyle = SpanStyle(
            color = colorResource(id = R.color.orange),
            fontFamily = pretendardFamily
        )

        if (shouldRunAcquirableTicketResetTimer(state.shareCount)) {
            withStyle(style = orangeStyle) {
                append(acquirableTicketResetTimer)
            }
            withStyle(style = orangePretendardStyle) {
                append(" " + stringResource(id = R.string.acquirable_ticket_reset_timer_text_suffix) + " ")
            }
            withStyle(style = pretendardFamilyStyle) {
                append(stringResource(id = R.string.acquirable_ticket_reset_info_text))
            }
        } else {
            withStyle(style = pretendardFamilyStyle) {
                append(stringResource(id = R.string.reward_popup_text_prefix))
            }
            withStyle(style = orangePretendardStyle) {
                append(" " + state.rewardBanner.shortName + " ")
            }
            withStyle(style = pretendardFamilyStyle) {
                append(stringResource(id = R.string.reward_popup_text_suffix))
            }
        }
    }
    return annotatedText
}

fun shouldRunAcquirableTicketResetTimer(shareCount: Int): Boolean {
    return shareCount >= 10
}
