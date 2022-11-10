package com.gleb.delineater.listeners

import com.gleb.delineater.data.entities.PictureEntity

interface MenuPictureListener {

    fun openExistPicture(picture: PictureEntity)

    fun openNewPicture()

    fun deletePicture(picture: PictureEntity)

}