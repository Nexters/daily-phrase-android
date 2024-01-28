package com.silvertown.android.dailyphrase.presentation.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun baseSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
    actionPerformed: () -> Unit = {},
    dismissed: () -> Unit = {},
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration
    )

    when (result) {
        SnackbarResult.ActionPerformed -> actionPerformed()
        SnackbarResult.Dismissed -> dismissed()
    }
}
