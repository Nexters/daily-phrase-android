package com.silvertown.android.dailyphrase.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.silvertown.android.dailyphrase.presentation.R

@Composable
fun ItemDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
        color = colorResource(id = R.color.divider)
    )
}

@Composable
fun GroupDivider() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = colorResource(id = R.color.divider)
    )
}
