package com.gleb.delineater.data.types

sealed class ColorPickerType {
    object BrushColorPicker: ColorPickerType()
    object BackgroundColorPicker: ColorPickerType()
}