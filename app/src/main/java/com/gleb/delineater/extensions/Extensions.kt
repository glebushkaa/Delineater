package com.gleb.delineater.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar

@SuppressLint("ShowToast")
fun Context.showToast(
    text: String,
    @Duration duration: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(this, text, duration).show()
}

@SuppressLint("ShowToast")
fun View.showSnackBar(
    text: String,
    @Duration duration: Int = Snackbar.LENGTH_SHORT,
    @ColorInt backgroundColor: Int? = null,
    @ColorInt textColor: Int? = null
) {
    val snackBar = Snackbar.make(this, text, duration)
    backgroundColor?.let {
        snackBar.setBackgroundTint(backgroundColor)
    }
    textColor?.let {
        snackBar.setTextColor(textColor)
    }
    snackBar.show()
}