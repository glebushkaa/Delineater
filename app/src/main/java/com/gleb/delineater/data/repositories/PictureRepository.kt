package com.gleb.delineater.data.repositories

import com.gleb.delineater.data.room.PictureDao
import com.gleb.delineater.data.entities.PictureEntity

class PictureRepository(
    private val pictureDao: PictureDao
) : PictureRepositoryInterface {

    override suspend fun getAllPictures() = pictureDao.getAllPictures()

    override suspend fun addNewPicture(picture: PictureEntity) {
        pictureDao.addPicture(picture)
    }

}