package com.silvertown.android.dailyphrase.presentation.ui.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.base.pretendardFamily

@Composable
fun NonLoginScreen(
    modifier: Modifier = Modifier,
    onClickKaKaoLogin: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_and_share_message),
            style = TextStyle(
                fontSize = 28.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onClickKaKaoLogin() },
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.yellow),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
                horizontal = 40.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.kakao_login),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(id = R.color.black)
            )
        }
    }
}
