package com.gleb.delineater.data.sealedClasses

sealed class PaintType {
    object Brush : PaintType()
    object Eraser : PaintType()
}