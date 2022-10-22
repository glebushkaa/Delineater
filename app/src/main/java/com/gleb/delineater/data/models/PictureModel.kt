package com.gleb.delineater.data.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PictureModel(
    val picture: Bitmap? = null
) : Parcelable