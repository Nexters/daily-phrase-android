package com.silvertown.android.dailyphrase.presentation.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.ui.base.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.ui.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.ui.component.GroupDivider
import com.silvertown.android.dailyphrase.presentation.ui.component.ItemDivider
import com.silvertown.android.dailyphrase.presentation.ui.component.ProfileContent

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit,
) {
    Content(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white)),
        navigateToBack = navigateToBack,
        navigateToUnsubscribe = navigateToUnsubscribe,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    navigateToBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit,
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
            MyPageBody(
                navigateToUnsubscribe = navigateToUnsubscribe,
            )
        }
    }
}

@Composable
private fun MyPageBody(
    modifier: Modifier = Modifier,
    navigateToUnsubscribe: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            userName = "TEST",
            userProfile = "https://cdn.pixabay.com/photo/2015/06/25/04/50/hand-print-820913_1280.jpg",
            email = "test@naver.com"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            GroupDivider()
            MyPageItem(
                modifier = Modifier,
                title = stringResource(id = R.string.share_app),
                action = {}
            )
            MyPageItem(
                modifier = Modifier,
                title = stringResource(id = R.string.leave_review),
                action = {}
            )

            GroupDivider()
            MyPageItem(
                modifier = Modifier,
                title = stringResource(id = R.string.logout),
                action = {}
            )
            UnsubscribeTextItem(
                modifier = Modifier,
                title = stringResource(id = R.string.unsubscribe_service),
                action = { navigateToUnsubscribe() }
            )
        }
    }
}

@Composable
fun MyPageItem(
    modifier: Modifier,
    title: String,
    action: () -> Unit,
) {
    ItemDivider()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.SemiBold
            ),
            color = colorResource(id = R.color.black)
        )
        IconButton(onClick = { action() }) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_arrow_forward_ios_24),
                contentDescription = null
            )
        }
    }
}

@Composable
fun UnsubscribeTextItem(
    modifier: Modifier,
    title: String,
    action: () -> Unit,
) {
    ItemDivider()
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 16.dp)
            .clickable { action() },
        text = title,
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = pretendardFamily,
            fontWeight = FontWeight.SemiBold
        ),
        color = colorResource(id = R.color.gray),
        textDecoration = TextDecoration.Underline,
    )
}
