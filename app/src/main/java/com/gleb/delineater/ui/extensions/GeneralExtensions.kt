package com.gleb.delineater.ui.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.GravityInt
import com.gleb.delineater.R
import com.google.android.material.button.MaterialButton
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
    backgroundColor: Int? = null,
    textColor: Int? = null,
    @GravityInt gravity: Int = Gravity.BOTTOM,
    action: Pair<String, View.OnClickListener>? = null
) {
    val snackBar = Snackbar.make(this, text, duration)
    backgroundColor?.let {
        snackBar.setBackgroundTint(resources.getColor(backgroundColor, null))
    }
    textColor?.let {
        snackBar.setTextColor(resources.getColor(textColor, null))
    }
    snackBar.setGravity(gravity)
    action?.let {
        snackBar.view.findViewById<TextView>(
            com.google.android.material.R.id.snackbar_action
        ).isAllCaps = false
        snackBar.setAction(it.first, it.second)
    }
    snackBar.show()
}

fun Snackbar.setGravity(@GravityInt gravity: Int) {
    val params = (view.layoutParams as FrameLayout.LayoutParams)
    params.gravity = gravity
    view.layoutParams = params
}

fun MaterialButton.setIconTint(color: Int) {
    iconTint = ColorStateList.valueOf(
        resources.getColor(color, null)
    )
}