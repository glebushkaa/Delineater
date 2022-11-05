package com.gleb.delineater.data

sealed class PaintType {
    object Brush : PaintType()
    object Eraser : PaintType()
}