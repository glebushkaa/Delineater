package com.gleb.delineater.ui.extensions

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.google.android.material.card.MaterialCardView

private const val CHANGE_BTN_SIZE_DURATION = 3000L
private const val PROGRESS_SAVE_DURATION = 1400L
private const val COLOR_DIALOG_TRANSLATION_DURATION = 300L

private const val REDUCE_VALUE = 0f
private const val INCREASE_VALUE = 1f
private const val REDUCE_SAVE_BUTTON_VALUE = 0.2f

private const val OUT_RIGHT_BORDER_TRANSLATION_X = -1000f
private const val CENTER_TRANSLATION_X = 0f

private const val ACTIVE_COLOR_BACKGROUND_BLUR_ALPHA = 0.6f

fun Button.reduceSaveBtnXSize(endAction: () -> Unit) {
    this.animate().scaleX(REDUCE_SAVE_BUTTON_VALUE).alpha(REDUCE_VALUE).also {
        it.startDelay = 0
        it.duration = 500
        it.withEndAction(endAction)
        it.start()
    }
}

fun Button.increaseSaveBtnXSize(endAction: () -> Unit) {
    this.animate().scaleX(INCREASE_VALUE).alpha(INCREASE_VALUE).also {
        it.startDelay = CHANGE_BTN_SIZE_DURATION
        it.withEndAction(endAction)
        it.start()
    }
}

fun ProgressBar.progressFadeAnimation() {
    this.animate().alpha(INCREASE_VALUE).also {
        it.duration = PROGRESS_SAVE_DURATION
        it.withEndAction {
            animate().alpha(REDUCE_VALUE).start()
        }
        it.start()
    }
}

fun Button.showWithFadeAnimation(alpha: Float) {
    this.visibility = View.VISIBLE
    this.animate().alpha(alpha).also {
        it.start()
    }
}

fun Button.hideWithFadeAnimation() {
    this.animate().alpha(REDUCE_VALUE).also {
        it.withEndAction {
            this.visibility = View.GONE
        }
        it.start()
    }
}

fun MaterialCardView.translateDialogByXCenter() {
    this.visibility = View.VISIBLE
    this.translationX = OUT_RIGHT_BORDER_TRANSLATION_X
    this.animate().alpha(INCREASE_VALUE).translationX(CENTER_TRANSLATION_X).also {
        it.duration = COLOR_DIALOG_TRANSLATION_DURATION
        it.start()
    }
}

fun MaterialCardView.translateDialogByXOverBorder() {
    this.animate().alpha(REDUCE_VALUE).translationX(OUT_RIGHT_BORDER_TRANSLATION_X).also {
        it.duration = COLOR_DIALOG_TRANSLATION_DURATION
        it.withEndAction {
            this.visibility = View.GONE
        }
        it.start()
    }
}

fun MaterialCardView.showWithFadeAnimation() {
    this.visibility = View.VISIBLE
    this.animate().alpha(ACTIVE_COLOR_BACKGROUND_BLUR_ALPHA).start()
}

fun MaterialCardView.hideWithFadeAnimation() {
    this.animate().alpha(REDUCE_VALUE).also {
        it.withEndAction {
            this.visibility = View.GONE
            it.start()
        }
    }
}