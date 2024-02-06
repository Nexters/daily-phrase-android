package com.silvertown.android.dailyphrase.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily

@Composable
fun DetailBottomAction(
    modifier: Modifier = Modifier,
    onClickLike: () -> Unit,
    onClickBookmark: () -> Unit,
    onClickShare: () -> Unit,
    isLike: Boolean,
    isBookmark: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            modifier = Modifier
                .padding(1.dp)
                .size(48.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClickLike()
                },
            painter = painterResource(
                if (isLike) R.drawable.ic_like_fill_60 else R.drawable.ic_like_60
            ),
            tint = Color.Unspecified,
            contentDescription = null,
        )
        Icon(
            modifier = Modifier
                .padding(1.dp)
                .size(48.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClickBookmark()
                },
            painter = painterResource(
                if (isBookmark) R.drawable.ic_bookmark_fill_60 else R.drawable.ic_bookmark_60
            ),
            tint = Color.Unspecified,
            contentDescription = null,
        )
        DetailKaKaoShareButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onClickShare
        )
    }
}

@Composable
private fun DetailKaKaoShareButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.orange),
            contentColor = colorResource(id = R.color.white)
        ),
        contentPadding = PaddingValues(
            vertical = 13.dp
        )
    ) {
        Text(
            text = stringResource(id = R.string.kakao_link_share),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.white)
        )
    }
}
