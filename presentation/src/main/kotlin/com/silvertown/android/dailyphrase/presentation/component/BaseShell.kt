package com.silvertown.android.dailyphrase.presentation.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.silvertown.android.dailyphrase.presentation.base.theme.DailyPhraseTheme
import com.silvertown.android.dailyphrase.presentation.base.theme.LocalBackgroundTheme

@Composable
fun DailyPhraseBaseShell(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable () -> Unit,
) {
    val color = LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation

    DailyPhraseTheme(
        darkTheme = isSystemInDarkTheme(),
        androidTheme = false,
        disableDynamicTheming = true,
    ) {
        Surface(
            color = if (color == Color.Unspecified) Color.Transparent else color,
            tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
            modifier = modifier.fillMaxSize(),
        ) {
            CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
                Scaffold(
                    modifier = modifier.fillMaxSize(),
                    topBar = topBar,
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier.padding(bottom = 70.dp)
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}
