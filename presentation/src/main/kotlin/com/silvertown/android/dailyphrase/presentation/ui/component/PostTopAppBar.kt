package com.silvertown.android.dailyphrase.presentation.ui.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.silvertown.android.dailyphrase.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = topAppBarColors(
        containerColor = colorResource(id = R.color.white),
        titleContentColor = colorResource(id = R.color.black),
    ),
    navigationContent: @Composable () -> Unit = {},
    actionsContent: @Composable () -> Unit = {},
    titleContent: @Composable () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = colors,
        navigationIcon = {
            navigationContent()
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
