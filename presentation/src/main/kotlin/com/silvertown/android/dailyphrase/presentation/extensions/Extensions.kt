package com.silvertown.android.dailyphrase.presentation.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.silvertown.android.dailyphrase.presentation.MainActivity
import com.silvertown.android.dailyphrase.presentation.R

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
