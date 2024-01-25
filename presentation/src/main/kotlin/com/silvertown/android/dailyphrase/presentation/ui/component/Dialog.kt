package com.silvertown.android.dailyphrase.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.base.pretendardFamily

@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        DialogContent(
            modifier = modifier,
            cornerRadius = 8.dp
        ) {
            content()
        }
    }
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    cornerRadius: Dp,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(cornerRadius),
            color = colorResource(id = R.color.white),
        ) {
            content()
        }
    }
}

@Composable
fun KakaoLoginDialog(
    modifier: Modifier = Modifier,
    onClickKaKaoLogin: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = modifier
            .widthIn(max = 270.dp)
            .padding(
                vertical = 20.dp,
                horizontal = 15.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_and_share_message),
            style = TextStyle(
                fontSize = 16.sp,
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
        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
                horizontal = 40.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.close),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.Normal
                ),
                color = colorResource(id = R.color.black)
            )
        }
    }
}

@Composable
fun LogoutDialog(
    modifier: Modifier = Modifier,
    onClickLogout: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = modifier
            .width(270.dp)
            .padding(
                top = 32.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.confirm_logout),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.orange),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
                horizontal = 40.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.keep_logged_in),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(id = R.color.white)
            )
        }
        Button(
            onClick = { onClickLogout() },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
                horizontal = 40.dp
            )
        ) {
            Text(
                text = stringResource(id = R.string.logout),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardFamily,
                    fontWeight = FontWeight.Normal
                ),
                color = colorResource(id = R.color.black)
            )
        }
    }
}
