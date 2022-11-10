package com.gleb.delineater.ui.extensions

import com.gleb.delineater.R
import com.gleb.delineater.data.ColorPickerType
import com.gleb.delineater.data.PaintType
import com.gleb.delineater.databinding.FragmentDrawBinding


fun FragmentDrawBinding.getColorFromPicker(colorPickerType: ColorPickerType, color: Int) {
    when (colorPickerType) {
        is ColorPickerType.BackgroundColorPicker -> setEraseColor(color)
        is ColorPickerType.BrushColorPicker -> setBrushColor(color)
    }
}

fun FragmentDrawBinding.enableBrush() {
    paintView.paintType = PaintType.Brush
    eraseBtn.setIconTint(R.color.gray_background)
    brushBtn.setIconTint(R.color.white)
}

fun FragmentDrawBinding.enableEraser() {
    paintView.paintType = PaintType.Eraser
    eraseBtn.setIconTint(R.color.white)
    brushBtn.setIconTint(R.color.gray_background)
}

fun FragmentDrawBinding.setColors() {
    colorPickerBtn.setBackgroundColor(paintView.brushColor)
    paintView.setBackgroundColor(paintView.eraserColor)
}

private fun FragmentDrawBinding.setBrushColor(color: Int) {
    enableBrush()
    paintView.brushColor = color
    colorPickerBtn.setBackgroundColor(color)
}

private fun FragmentDrawBinding.setEraseColor(color: Int) {
    enableEraser()
    paintView.eraserColor = color
    paintView.setBackgroundColor(color)
    paintView.updateEraseColor(color)
}