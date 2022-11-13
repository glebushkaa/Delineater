package com.gleb.delineater.data.sealedClasses

sealed class ColorPickerType {
    object BrushColorPicker: ColorPickerType()
    object BackgroundColorPicker: ColorPickerType()
}