package com.gleb.delineater.domain.usecases.newPicture

import android.graphics.Picture
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepository

class NewPictureUseCase(private val pictureRepository: PictureRepository) {

    suspend fun addNewPicture(picture: PictureEntity) {
        pictureRepository.addNewPicture(picture)
    }

}