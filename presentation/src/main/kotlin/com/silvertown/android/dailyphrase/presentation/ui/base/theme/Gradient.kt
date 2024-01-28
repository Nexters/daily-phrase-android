package com.silvertown.android.dailyphrase.presentation.ui.base.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * @param top  // Gradient의 상단 색상
 * @param bottom // Gradient의 하단 색상
 * @param container // Gradient가 적용되는 컨테이너 색상
 */
@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

val LocalGradientColors =
    staticCompositionLocalOf { GradientColors() }
