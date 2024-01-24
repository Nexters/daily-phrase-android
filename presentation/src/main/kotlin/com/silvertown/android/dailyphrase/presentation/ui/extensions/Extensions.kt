package com.silvertown.android.dailyphrase.presentation.ui.extensions

import android.content.Context

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}
