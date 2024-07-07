package com.silvertown.android.dailyphrase.presentation.ui.reward

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily

@Composable
internal fun RewardPopup(
    rewardBanner: RewardBanner,
) {
    val annotatedText = buildAnnotatedString {
        append(stringResource(id = R.string.reward_popup_text_prefix))
        withStyle(style = SpanStyle(color = colorResource(id = R.color.orange))) {
            append(" " + rewardBanner.shortName + " ")
        }
        append(stringResource(id = R.string.reward_popup_text_suffix))
    }

    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -30) // 하단부터 팝업포지션 offset
    ) {
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
                    text = annotatedText, style = TextStyle(
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
