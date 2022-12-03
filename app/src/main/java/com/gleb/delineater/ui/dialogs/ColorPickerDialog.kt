package com.gleb.delineater.ui.dialogs

import com.gleb.delineater.databinding.DialogColorPickBinding
import com.gleb.delineater.ui.extensions.blurFadeAnim
import com.gleb.delineater.ui.extensions.hideFadeAnim
import com.gleb.delineater.ui.extensions.translateDialogByXCenter
import com.gleb.delineater.ui.extensions.translateDialogByXOverBorder
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode

class ColorPickerDialog(private val colorPickerBinding: DialogColorPickBinding) {

    var colorListener: ((Int) -> Unit)? = null

    fun initClickListeners() {
        colorPickerBinding.apply {
            cancelBtn.setOnClickListener {
                hide()
            }
            confirmBtn.setOnClickListener {
                hide()
                colorListener?.invoke(colorPickerView.color)
            }
            colorPickerBlur.setOnClickListener {
                hide()
            }
            dialogCardView.isClickable = true
        }
    }

    fun setColorPickView() {
        val bubbleFlag = BubbleFlag(colorPickerBinding.colorPickerBlur.context)
        bubbleFlag.flagMode = FlagMode.FADE
        colorPickerBinding.colorPickerView.flagView = bubbleFlag
    }

    fun show() {
        colorPickerBinding.apply {
            colorPickerBlur.blurFadeAnim()
            dialogCardView.translateDialogByXCenter()
        }
    }

    fun hide() {
        colorPickerBinding.apply {
            colorPickerBlur.hideFadeAnim()
            dialogCardView.translateDialogByXOverBorder()
        }
    }

}