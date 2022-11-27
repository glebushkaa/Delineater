package com.gleb.delineater.domain

import com.gleb.delineater.data.entities.PictureEntity

interface PictureRepository {

    suspend fun getAllPictures(): List<PictureEntity>

    suspend fun addNewPicture(picture: PictureEntity): Long

    suspend fun updatePicture(picture: PictureEntity)

    suspend fun deletePicture(picture: PictureEntity)
}

