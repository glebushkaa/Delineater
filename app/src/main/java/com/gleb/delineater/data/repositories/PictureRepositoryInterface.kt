package com.gleb.delineater.data.repositories

import com.gleb.delineater.data.entities.PictureEntity

interface PictureRepositoryInterface {

    suspend fun getAllPictures(): List<PictureEntity>

    suspend fun addNewPicture(picture: PictureEntity)

    suspend fun updatePicture(picture: PictureEntity)

    suspend fun deletePicture(picture: PictureEntity)
}

