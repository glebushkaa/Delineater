package com.gleb.delineater.ui.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.GravityInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.gleb.delineater.R
import com.google.android.material.behavior.SwipeDismissBehavior
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
    action: Pair<String, View.OnClickListener>? = null,
    actionColor: Int? = null
) {
    val snackBar = Snackbar.make((this as CoordinatorLayout), text, duration)
    backgroundColor?.let { snackBar.setBackgroundTint(resources.getColor(backgroundColor, null)) }
    textColor?.let { snackBar.setTextColor(resources.getColor(textColor, null)) }
    snackBar.setGravity(gravity)
    action?.let { snackBar.addAction(it, actionColor) }
    snackBar.show()
}

fun Snackbar.addAction(action: Pair<String, View.OnClickListener>, actionColor: Int? = null) {
    view.findViewById<MaterialButton>(
        com.google.android.material.R.id.snackbar_action
    ).apply {
        isAllCaps = false
        letterSpacing = 0f
        rippleColor = resources.getColorStateList(android.R.color.transparent, null)
        actionColor?.let { setTextColor(resources.getColor(it, null)) }
    }
    setAction(action.first, action.second)
}

fun Snackbar.setGravity(@GravityInt gravity: Int) {
    val params = (view.layoutParams as CoordinatorLayout.LayoutParams)
    params.gravity = gravity
    view.layoutParams = params
}

fun MaterialButton.setIconTint(color: Int) {
    iconTint = ColorStateList.valueOf(
        resources.getColor(color, null)
    )
}