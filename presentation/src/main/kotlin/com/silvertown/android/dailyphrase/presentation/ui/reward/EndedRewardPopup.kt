package com.silvertown.android.dailyphrase.presentation.ui.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.presentation.R

@Composable
fun EndedRewardPopup(
    eventMonth: Int,
    navigateToEventPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 30.dp), // 하단부터 팝업포지션 offset,
        contentAlignment = Alignment.BottomCenter
    ) {
        EndedPopupContainer(
            popupText = stringResource(id = R.string.home_reward_check_ended_event, eventMonth),
            navigateToEventPage = navigateToEventPage
        )
    }
}

@Composable
private fun EndedPopupContainer(
    popupText: String,
    navigateToEventPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .heightIn(min = 40.dp)
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.disabled_tooltip_border),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(color = colorResource(id = R.color.white))
            .clickable {
                navigateToEventPage()
            }
            .padding(
                horizontal = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Text(
            text = popupText,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            ),
            color = colorResource(id = R.color.black)
        )
    }
}