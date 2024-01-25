package com.silvertown.android.dailyphrase.presentation.ui.mypage.non_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.base.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.ui.component.BaseTopAppBar

@Composable
fun NonLoginScreen(
    modifier: Modifier = Modifier,
    nonLoginViewModel: NonLoginViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
    onClickKaKaoLogin: () -> Unit,
) {
    Content(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white)),
        navigateToBack = navigateToBack,
        onClickKaKaoLogin = onClickKaKaoLogin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    navigateToBack: () -> Unit,
    onClickKaKaoLogin: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                navigationContent = {
                    IconButton(onClick = { navigateToBack() }) {
                        Icon(
                            painterResource(id = R.drawable.ic_arrow_back_24),
                            contentDescription = null
                        )
                    }
                },
                titleContent = {
                    Text(
                        text = stringResource(id = R.string.my_page_title),
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontFamily = pretendardFamily,
                            fontWeight = FontWeight.Medium
                        ),
                        color = colorResource(id = R.color.black)
                    )
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NonLoginBody(
                modifier = Modifier,
                onClickKaKaoLogin = onClickKaKaoLogin
            )
        }
    }
}

@Composable
private fun NonLoginBody(
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
