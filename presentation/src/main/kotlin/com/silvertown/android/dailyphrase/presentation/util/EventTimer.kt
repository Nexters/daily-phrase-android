package com.silvertown.android.dailyphrase.presentation.util

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.silvertown.android.dailyphrase.presentation.R
import com.silvertown.android.dailyphrase.presentation.base.theme.pretendardFamily
import com.silvertown.android.dailyphrase.presentation.util.Constants.TWO_MINUTES_IN_MILLIS
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Composable
internal fun EventTimer(
    modifier: Modifier = Modifier,
    eventEndedTime: LocalDateTime?,
    onTimeBelowThreshold: () -> Unit,
) {
    var remainTime by remember {
        mutableLongStateOf(
            Duration.between(
                LocalDateTime.now(),
                eventEndedTime
            ).toMillis()
        )
    }

    LaunchedEffect(Unit) {
        while (remainTime > 0) {
            delay(1000)
            remainTime = Duration.between(LocalDateTime.now(), eventEndedTime).toMillis()
            if (remainTime <= TWO_MINUTES_IN_MILLIS) {
                onTimeBelowThreshold()
                break
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.event_timer_text, formatTime(remainTime)),
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = pretendardFamily,
                fontWeight = FontWeight.Medium
            ),
            color = colorResource(id = R.color.white)
        )
    }
}

private fun formatTime(time: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(time)
    val min = TimeUnit.MILLISECONDS.toMinutes(time) % 60
    val sec = TimeUnit.MILLISECONDS.toSeconds(time) % 60

    return String.format("%02d:%02d:%02d", hours, min, sec)
}
