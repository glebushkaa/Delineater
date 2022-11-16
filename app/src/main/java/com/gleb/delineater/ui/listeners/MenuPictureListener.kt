package com.gleb.delineater.ui.listeners

import android.widget.ImageView
import com.gleb.delineater.data.entities.PictureEntity

interface MenuPictureListener {

    fun openExistPicture(picture: PictureEntity)

    fun openNewPicture()

    fun deletePicture(picture: PictureEntity)

}