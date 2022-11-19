package com.gleb.delineater.ui.types

sealed class ColorPickerType {
    object BrushColorPicker: ColorPickerType()
    object BackgroundColorPicker: ColorPickerType()
}