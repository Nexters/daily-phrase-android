package com.silvertown.android.dailyphrase.presentation.ui.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.silvertown.android.dailyphrase.domain.model.RewardBanner
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.component.KaKaoLoginButton

@Composable
fun HomeRewardBanner(
    rewardBanner: RewardBanner,
    eventMonth: Int,
    canCheckThisMonthRewardResult: () -> Boolean,
    modifier: Modifier = Modifier,
    onClickKaKaoLogin: () -> Unit = {},
    navigateToEventPage: () -> Unit = {},
) {
    val ongoingEventString = buildAnnotatedString {
        append(stringResource(id = R.string.login_prompt))
        append("\n")
        withStyle(style = SpanStyle(color = colorResource(id = R.color.orange))) {
            append(rewardBanner.shortName)
        }
        append(" " + stringResource(id = R.string.claim_reward))
    }

    val endedEventString = buildAnnotatedString {
        append(stringResource(id = R.string.reward_date_suffix, eventMonth))
        append("\n")
        append(stringResource(id = R.string.home_reward_announcement_winner))
    }

    val isEndedEvent = canCheckThisMonthRewardResult()

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.orange),
                        colorResource(id = R.color.vivid_red)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .padding(top = 10.dp, bottom = 20.dp, start = 24.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.reward_date_suffix, eventMonth),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = pretendardFamily,
                        fontWeight = FontWeight.Medium
                    ),
                    color = colorResource(id = R.color.gray)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isEndedEvent) {
                        endedEventString
                    } else {
                        ongoingEventString
                    },
                    style = TextStyle(
                        fontSize = 22.sp,
                        lineHeight = 32.sp,
                        fontFamily = pretendardFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = colorResource(id = R.color.black)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(35.dp))
                        .background(
                            color = colorResource(id = R.color.orange).copy(
                                alpha = 0.12f
                            )
                        )
                        .padding(vertical = 7.dp, horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.reward_participation_suffix,
                            rewardBanner.totalParticipantCount
                        ),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = pretendardFamily,
                            fontWeight = FontWeight.Medium
                        ),
                        color = colorResource(id = R.color.orange)
                    )
                }
            }

            AsyncImage(
                modifier = Modifier.size(160.dp),
                model = rewardBanner.imageUrl,
                contentDescription = null
            )
        }

        if (isEndedEvent) {
            ConfirmEntryResultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 14.dp), // Column의 10.dp + 14.dp = 24.dp
                navigateToEventPage = navigateToEventPage
            )
        } else {
            KaKaoLoginButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 14.dp), // Column의 10.dp + 14.dp = 24.dp
                title = R.string.simple_login,
                onClickKaKaoLogin = onClickKaKaoLogin,
            )
        }
    }
}

@Composable
private fun ConfirmEntryResultButton(
    navigateToEventPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { navigateToEventPage() },
        modifier = modifier
            .heightIn(min = 48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.orange),
            contentColor = colorResource(id = R.color.white)
        )
    ) {
        Text(
            text = stringResource(id = R.string.confirm_entry_result),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.white)
        )
    }
}