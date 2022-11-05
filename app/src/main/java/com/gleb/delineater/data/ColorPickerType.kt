package com.gleb.delineater.data

sealed class ColorPickerType {
    object BrushColorPicker: ColorPickerType()
    object BackgroundColorPicker: ColorPickerType()
}