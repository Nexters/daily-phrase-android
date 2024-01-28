package com.silvertown.android.dailyphrase.presentation.base.theme


import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.silvertown.android.dailyphrase.presentation.R

/**
 * Daily phrase mvp의 테마는 임시 Default, Andorid 다크 라이트 동일
 */
@VisibleForTesting
val LightDefaultColorScheme = lightColorScheme(
    primary = Color(R.color.orange),
    onPrimary = Color.White,
    primaryContainer = Color(R.color.gray),
    onPrimaryContainer = Color(R.color.light_gray),
    secondary = Color.Black,
    onSecondary = Color.White,
    secondaryContainer = Color(R.color.orange),
    onSecondaryContainer = Color.White,
    tertiary = Color(R.color.gray),
    onTertiary = Color.White,
    tertiaryContainer = Color(R.color.gray),
    onTertiaryContainer = Color(R.color.light_gray),
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    outline = Color(R.color.divider),
)

@VisibleForTesting
val DarkDefaultColorScheme = darkColorScheme(
    primary = Color(R.color.orange),
    onPrimary = Color.White,
    primaryContainer = Color(R.color.gray),
    onPrimaryContainer = Color(R.color.light_gray),
    secondary = Color.Black,
    onSecondary = Color.White,
    secondaryContainer = Color(R.color.orange),
    onSecondaryContainer = Color.White,
    tertiary = Color(R.color.gray),
    onTertiary = Color.White,
    tertiaryContainer = Color(R.color.gray),
    onTertiaryContainer = Color(R.color.light_gray),
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    outline = Color(R.color.divider),
)

@VisibleForTesting
val LightAndroidColorScheme = lightColorScheme(
    primary = Color(R.color.orange),
    onPrimary = Color.White,
    primaryContainer = Color(R.color.gray),
    onPrimaryContainer = Color(R.color.light_gray),
    secondary = Color.Black,
    onSecondary = Color.White,
    secondaryContainer = Color(R.color.orange),
    onSecondaryContainer = Color.White,
    tertiary = Color(R.color.gray),
    onTertiary = Color.White,
    tertiaryContainer = Color(R.color.gray),
    onTertiaryContainer = Color(R.color.light_gray),
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    outline = Color(R.color.divider),
)

@VisibleForTesting
val DarkAndroidColorScheme = darkColorScheme(
    primary = Color(R.color.orange),
    onPrimary = Color.White,
    primaryContainer = Color(R.color.gray),
    onPrimaryContainer = Color(R.color.light_gray),
    secondary = Color.Black,
    onSecondary = Color.White,
    secondaryContainer = Color(R.color.orange),
    onSecondaryContainer = Color.White,
    tertiary = Color(R.color.gray),
    onTertiary = Color.White,
    tertiaryContainer = Color(R.color.gray),
    onTertiaryContainer = Color(R.color.light_gray),
    error = Color.Red,
    onError = Color.White,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    outline = Color(R.color.divider),
)

val LightAndroidGradientColors = GradientColors(container = Color.Black)
val DarkAndroidGradientColors = GradientColors(container = Color.Black)
val LightAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)

/**
 * @param darkTheme 테마가 어두운 색 구성표를 사용할지 여부 => 기본적으로 사용자의 시스템 설정을 따름
 * @param androidTheme 앱의 테마가 기본 테마 대신 Android 테마 색상표를 사용해야 하는지 여부
 * @param disableDynamicTheming 동적 테마 사용을 비활성화 할지를 결정. 동적 테마를 지원하는 경우, 앱은 사용자의 시스템 설정에 따라 테마의 색상을 동적으로 변경
 */
@Composable
fun DailyPhraseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    androidTheme: Boolean = false,
    disableDynamicTheming: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        androidTheme -> if (darkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
        !disableDynamicTheming && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        else -> if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    }
    val emptyGradientColors =
        GradientColors(container = colorScheme.surfaceColorAtElevation(2.dp))

    val defaultGradientColors = GradientColors(
        top = colorScheme.inverseOnSurface,
        bottom = colorScheme.primaryContainer,
        container = colorScheme.surface,
    )
    val gradientColors = when {
        androidTheme -> if (darkTheme) DarkAndroidGradientColors else LightAndroidGradientColors
        !disableDynamicTheming && supportsDynamicTheming() -> emptyGradientColors
        else -> defaultGradientColors
    }

    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
    )
    val backgroundTheme = when {
        androidTheme -> if (darkTheme) DarkAndroidBackgroundTheme else LightAndroidBackgroundTheme
        else -> defaultBackgroundTheme
    }
    val tintTheme = when {
        androidTheme -> TintTheme()
        !disableDynamicTheming && supportsDynamicTheming() -> TintTheme(colorScheme.primary)
        else -> TintTheme()
    }

    CompositionLocalProvider(
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
