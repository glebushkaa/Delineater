package com.gleb.delineater.ui.extensions

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.google.android.material.card.MaterialCardView

//Duration
private const val CHANGE_BTN_SIZE_DURATION = 3000L
private const val PROGRESS_SAVE_DURATION = 1400L
private const val COLOR_DIALOG_TRANSLATION_DURATION = 300L
private const val SAVE_BUTTON_DURATION = 500L

//Size
private const val DEFAULT_SCALE_SIZE = 1f
private const val REDUCE_SAVE_BUTTON_VALUE = 0.2f

//Translation position
private const val OUT_RIGHT_BORDER_TRANSLATION_X = -1000f
private const val CENTER_TRANSLATION_X = 0f

//Alpha
private const val DEFAULT_ALPHA = 1f
private const val DEFAULT_REDUCE_ALPHA = 0f
private const val ACTIVE_COLOR_BACKGROUND_BLUR_ALPHA = 0.6f

//Delay
private const val DEFAULT_START_DELAY = 0L

fun Button.animSaving(progressBar: ProgressBar, endAction: () -> Unit) {
    this.downscaleSaveBtnX {
        progressBar.animSaveProgress()
        this.upscaleSaveBtnX(endAction)
    }
}

fun Button.downscaleSaveBtnX(endAction: () -> Unit) {
    this.animate().scaleX(REDUCE_SAVE_BUTTON_VALUE).alpha(DEFAULT_REDUCE_ALPHA).also {
        it.startDelay = DEFAULT_START_DELAY
        it.duration = SAVE_BUTTON_DURATION
        it.withEndAction(endAction)
        it.start()
    }
}

fun Button.upscaleSaveBtnX(endAction: () -> Unit) {
    this.animate().scaleX(DEFAULT_SCALE_SIZE).alpha(DEFAULT_ALPHA).also {
        it.startDelay = CHANGE_BTN_SIZE_DURATION
        it.withEndAction(endAction)
        it.start()
    }
}

fun ProgressBar.animSaveProgress() {
    this.animate().alpha(DEFAULT_SCALE_SIZE).also {
        it.duration = PROGRESS_SAVE_DURATION
        it.withEndAction {
            animate().alpha(DEFAULT_REDUCE_ALPHA).start()
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
    this.animate().alpha(DEFAULT_REDUCE_ALPHA).also {
        it.withEndAction {
            this.visibility = View.GONE
        }
        it.start()
    }
}

fun MaterialCardView.translateDialogByXCenter() {
    this.visibility = View.VISIBLE
    this.translationX = OUT_RIGHT_BORDER_TRANSLATION_X
    this.animate().alpha(DEFAULT_SCALE_SIZE).translationX(CENTER_TRANSLATION_X).also {
        it.duration = COLOR_DIALOG_TRANSLATION_DURATION
        it.start()
    }
}

fun MaterialCardView.translateDialogByXOverBorder() {
    this.animate().alpha(DEFAULT_REDUCE_ALPHA).translationX(OUT_RIGHT_BORDER_TRANSLATION_X).also {
        it.duration = COLOR_DIALOG_TRANSLATION_DURATION
        it.withEndAction {
            this.visibility = View.GONE
        }
        it.start()
    }
}

fun View.showWithFadeAnimation(alpha: Float = DEFAULT_ALPHA) {
    this.visibility = View.VISIBLE
    this.animate().alpha(alpha).start()
}

fun View.hideWithFadeAnimation(endAction: (() -> Unit)? = null) {
    this.animate().alpha(DEFAULT_REDUCE_ALPHA).also {
        it.withEndAction {
            this.visibility = View.GONE
            endAction?.invoke()
        }
        it.start()
    }
}