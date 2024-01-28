package com.silvertown.android.dailyphrase.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.silvertown.android.dailyphrase.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = topAppBarColors(
        containerColor = colorResource(id = R.color.white),
        titleContentColor = colorResource(id = R.color.black),
    ),
    navigateToBack: () -> Unit = {},
    actionsContent: @Composable () -> Unit = {},
    titleContent: @Composable () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        modifier = modifier,
        colors = colors,
        navigationIcon = {
            IconButton(onClick = { navigateToBack() }) {
                Icon(
                    painterResource(id = R.drawable.ic_arrow_back_24),
                    contentDescription = null
                )
            }
        },
        title = {
            titleContent()
        },
        actions = {
            actionsContent()
        },
        scrollBehavior = scrollBehavior,
    )
}
