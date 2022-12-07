package com.gleb.delineater.domain.usecases

import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepositoryImpl

class UpdatePictureUseCase(
    private val pictureRepository: PictureRepositoryImpl
) {

    suspend fun updatePicture(picture: PictureEntity) {
        pictureRepository.updatePicture(picture)
    }

}