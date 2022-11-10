package com.gleb.delineater.ui.extensions

import android.view.View
import android.widget.Button
import android.widget.ProgressBar

private const val CHANGE_BTN_SIZE_DURATION = 3000L
private const val PROGRESS_SAVE_DURATION = 1400L

private const val REDUCE_VALUE = 0f
private const val INCREASE_VALUE = 1f

fun Button.reduceButtonSizeX(endAction: () -> Unit) {
    animate().scaleX(REDUCE_VALUE).alpha(REDUCE_VALUE).also {
        it.startDelay = 0
        it.withEndAction(endAction)
        it.start()
    }
}

fun Button.increaseButtonSizeX(endAction: () -> Unit) {
    animate().scaleX(INCREASE_VALUE).alpha(INCREASE_VALUE).also {
        it.startDelay = CHANGE_BTN_SIZE_DURATION
        it.withEndAction(endAction)
        it.start()
    }
}

fun ProgressBar.progressFadeAnimation() {
    animate().alpha(INCREASE_VALUE).also {
        it.duration = PROGRESS_SAVE_DURATION
        it.withEndAction {
            animate().alpha(REDUCE_VALUE).start()
        }
        it.start()
    }
}

fun Button.showWithFadeAnimation(alpha: Float) {
    this.visibility = View.VISIBLE
    animate().alpha(alpha).also {
        it.start()
    }
}

fun Button.hideWithFadeAnimation() {
    animate().alpha(REDUCE_VALUE).also {
        it.withEndAction {
            this.visibility = View.GONE
        }
        it.start()
    }
}