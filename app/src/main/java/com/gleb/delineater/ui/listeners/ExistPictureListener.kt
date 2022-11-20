package com.gleb.delineater.ui.listeners

import com.gleb.delineater.data.entities.PictureEntity

interface ExistPictureListener : BaseAdapterListener {

    fun deletePicture(picture: PictureEntity)

    fun openPicture(picture: PictureEntity)

}