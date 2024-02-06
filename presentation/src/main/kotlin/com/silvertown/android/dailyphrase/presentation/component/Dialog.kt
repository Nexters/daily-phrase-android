package com.silvertown.android.dailyphrase.presentation.component

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily

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
    @StringRes message: Int,
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
            text = stringResource(id = message),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        KaKaoLoginButton(
            modifier = Modifier.fillMaxWidth(),
            onClickKaKaoLogin = onClickKaKaoLogin,
        )

        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
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
                bottom = 20.dp,
                start = 20.dp,
                end = 20.dp
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
        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { onDismissRequest() },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.orange),
                contentColor = colorResource(id = R.color.white)
            ),
            contentPadding = PaddingValues(
                vertical = 9.dp,
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

class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.item_loading)
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
