package com.gleb.delineater.listeners

import com.gleb.delineater.data.entities.PictureEntity

interface MenuPictureListener {

    fun showAddPictureInfo(text: String)

    fun showPictureInfo(text: String,picture: PictureEntity)

}