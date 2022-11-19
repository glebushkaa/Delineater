package com.gleb.delineater.ui.types

sealed class PaintType {
    object Brush : PaintType()
    object Eraser : PaintType()
}