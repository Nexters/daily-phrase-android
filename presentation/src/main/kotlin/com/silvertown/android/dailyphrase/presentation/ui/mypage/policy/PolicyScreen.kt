package com.silvertown.android.dailyphrase.presentation.ui.mypage.policy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.silvertown.android.dailyphrase.presentation.component.BaseTopAppBar
import com.silvertown.android.dailyphrase.presentation.component.BaseWebView
import com.silvertown.android.dailyphrase.presentation.component.DailyPhraseBaseShell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolicyScreen(
    modifier: Modifier = Modifier,
    navigateToBack: () -> Unit,
) {
    DailyPhraseBaseShell(
        modifier = modifier,
        topBar = {
            BaseTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigateToBack = { navigateToBack() },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BaseWebView(url = "https://sponge-wood-68d.notion.site/d63b4e79dcaf4c98936daff1d271c0d1")
        }
    }
}
