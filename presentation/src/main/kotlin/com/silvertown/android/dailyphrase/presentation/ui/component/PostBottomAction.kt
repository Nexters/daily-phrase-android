package com.silvertown.android.dailyphrase.presentation.ui.component

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.silvertown.android.dailyphrase.presentation.R

@Composable
fun PostBottomAction(
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
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
                .clickable { onFavoriteClick() },
            painter = painterResource(id = R.drawable.ic_favorite_24),
            contentDescription = null,
        )
        Icon(
            modifier = Modifier
                .padding(1.dp)
                .size(48.dp)
                .clickable { onBookmarkClick() },
            painter = painterResource(id = R.drawable.ic_bookmark_24),
            contentDescription = null,
        )
        PostKaKaoShareButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onShareClick
        )
    }
}

@Composable
private fun PostKaKaoShareButton(
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
            color = colorResource(id = R.color.white)
        )
    }
}
