package com.silvertown.android.dailyphrase.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.base.theme.pretendardFamily

@Composable
fun KaKaoLoginButton(
    modifier: Modifier = Modifier,
    onClickKaKaoLogin: () -> Unit,
) {
    Button(
        onClick = { onClickKaKaoLogin() },
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.kakao_container),
            contentColor = colorResource(id = R.color.white)
        ),
        contentPadding = PaddingValues(
            vertical = 9.dp,
            horizontal = 12.dp
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = R.drawable.ic_kakao_symbol),
                tint = colorResource(id = R.color.black),
                contentDescription = null
            )

            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = stringResource(id = R.string.kakao_login),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(id = R.color.kakao_text)
            )
        }
    }
}
