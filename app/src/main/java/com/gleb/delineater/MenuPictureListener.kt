package com.gleb.delineater

import android.graphics.Bitmap

interface MenuPictureListener {

    fun showAddPictureInfo(text: String)

    fun showPictureInfo(text: String,bitmap: Bitmap)

}