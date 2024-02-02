package com.silvertown.android.dailyphrase.presentation.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R
import java.text.NumberFormat
import java.util.Locale

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density + 0.5f).toInt()
}

fun Context.navigateToStart(
    message: Int,
) {
    Toast.makeText(this, this.getString(message), Toast.LENGTH_SHORT).show()
    val intent = Intent(this, MainActivity::class.java)
    this.startActivity(intent)
    (this as? Activity)?.finish()
}

fun Int.formatNumberWithComma(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return formatter.format(this)
}

fun Long.formatNumberWithComma(): String {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
    return formatter.format(this)
}
