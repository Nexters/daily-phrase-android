package com.silvertown.android.dailyphrase.presentation.ui.mypage

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silvertown.android.dailyphrase.domain.model.Member
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.component.BaseDialog
import com.silvertown.android.dailyphrase.presentation.component.DailyPhraseBaseShell
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.component.GroupDivider
import com.silvertown.android.dailyphrase.presentation.component.ItemDivider
import com.silvertown.android.dailyphrase.presentation.component.LogoutDialog
import com.silvertown.android.dailyphrase.presentation.component.ProfileContent
import com.silvertown.android.dailyphrase.presentation.extensions.navigateToStart

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier,
    myPageViewModel: MyPageViewModel = hiltViewModel(),
    navigateToBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit,
    navigateToPolicy: () -> Unit,
) {
    val myPageUiState by myPageViewModel.myPageUiState.collectAsStateWithLifecycle()
    val memberData by myPageViewModel.memberData.collectAsStateWithLifecycle()

    Content(
        modifier = modifier,
        navigateToBack = navigateToBack,
        navigateToUnsubscribe = navigateToUnsubscribe,
        navigateToPolicy = navigateToPolicy,
        onClickLogout = myPageViewModel::logout,
        showLogoutDialog = myPageViewModel::showLogoutDialog,
        myPageUiState = myPageUiState,
        memberData = memberData,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    navigateToBack: () -> Unit,
    navigateToUnsubscribe: () -> Unit,
    navigateToPolicy: () -> Unit,
    onClickLogout: () -> Unit,
    showLogoutDialog: (Boolean) -> Unit,
    myPageUiState: MyPageUiState,
    memberData: Member,
) {
    val context = LocalContext.current

    if (myPageUiState.showLogoutDialog) {
        BaseDialog(
            modifier = Modifier,
            onDismissRequest = { showLogoutDialog(false) }
        ) {
            LogoutDialog(
                onClickLogout = {
                    onClickLogout()
                    context.navigateToStart(R.string.success_logout)
                },
                onDismissRequest = { showLogoutDialog(false) }
            )
        }
    }

    DailyPhraseBaseShell(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigateToBack = { navigateToBack() },
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
                }
            )
        }
    ) {
        MyPageBody(
            context = context,
            navigateToUnsubscribe = navigateToUnsubscribe,
            navigateToPolicy = navigateToPolicy,
            showLogoutDialog = showLogoutDialog,
            memberData = memberData
        )
    }
}

@Composable
private fun MyPageBody(
    modifier: Modifier = Modifier,
    context: Context,
    navigateToUnsubscribe: () -> Unit,
    navigateToPolicy: () -> Unit,
    showLogoutDialog: (Boolean) -> Unit,
    memberData: Member,
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
            userName = memberData.name,
            userProfile = memberData.imageUrl,
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
                action = {
                    Toast.makeText(context, R.string.feature_under_construction, Toast.LENGTH_SHORT)
                        .show()
                }
            )
            MyPageItem(
                modifier = Modifier,
                title = stringResource(id = R.string.leave_review),
                action = {
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
                        .also { intent ->
                            context.startActivity(intent)
                        }
                },
            )
            GroupDivider()
            MyPageItem(
                modifier = Modifier,
                title = stringResource(id = R.string.privacy_policy_title),
                action = { navigateToPolicy() }
            )
            GroupDivider()
            MyPageItem(
                modifier = Modifier,
                title = stringResource(id = R.string.logout),
                action = { showLogoutDialog(true) }
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
            .clickable { action() }
            .padding(16.dp),
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
        Icon(
            modifier = Modifier.size(12.dp),
            painter = painterResource(id = R.drawable.ic_arrow_forward_ios_24),
            contentDescription = null
        )
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
