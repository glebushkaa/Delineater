package com.gleb.delineater.data.types

sealed class PaintType {
    object Brush : PaintType()
    object Eraser : PaintType()
}