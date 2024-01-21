package com.silvertown.android.dailyphrase.presentation.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.silvertown.android.dailyphrase.presentation.R

@Composable
fun PostBottomAction(
    modifier: Modifier,
    onFavoriteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            PostActionBox(
                icon = R.drawable.ic_favorite_24,
                title = R.string.favorite,
                onClick = onFavoriteClick
            )
            PostActionBox(
                icon = R.drawable.ic_bookmark_24,
                title = R.string.bookmark,
                onClick = onBookmarkClick
            )
            PostActionBox(
                icon = R.drawable.ic_share_24,
                title = R.string.share,
                onClick = onShareClick
            )
        }
    }
}

@Composable
private fun PostActionBox(
    @DrawableRes icon: Int,
    @StringRes title: Int,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .heightIn(max = 44.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(id = title)
            )
        }
    }
}
