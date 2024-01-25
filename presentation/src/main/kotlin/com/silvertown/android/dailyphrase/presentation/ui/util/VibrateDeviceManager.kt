package com.silvertown.android.dailyphrase.presentation.ui.util


import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

class VibrateDeviceManager(context: Context) {
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrateFor(time: Long) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                time,
                VibrationEffect.DEFAULT_AMPLITUDE,
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun vibrateSingle(context: Context) {
    val vibrator = VibrateDeviceManager(context)
    vibrator.vibrateFor(200)
}
