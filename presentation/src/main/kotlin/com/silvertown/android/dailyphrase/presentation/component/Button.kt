package com.silvertown.android.dailyphrase.presentation.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.silvertown.android.dailyphrase.presentation.util.speakTTS
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

@Composable
fun SpeakButton(
    context: Context,
    message: String,
) {
    val coroutineScope = rememberCoroutineScope()
    var isClickable by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .clickable(enabled = isClickable) {
                speakTTS(context, message)
                isClickable = false
                coroutineScope.launch {
                    delay(3000)
                    isClickable = true
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_audio),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Text(
            text = stringResource(id = R.string.speak_tts),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Medium
            ),
            color = colorResource(id = R.color.black)
        )
    }
}
