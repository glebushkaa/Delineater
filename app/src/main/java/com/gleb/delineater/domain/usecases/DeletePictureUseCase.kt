package com.gleb.delineater.domain.usecases

import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepositoryImpl

class DeletePictureUseCase(
    private val pictureRepository: PictureRepositoryImpl
) {

    suspend fun deletePicture(picture: PictureEntity) {
        pictureRepository.deletePicture(picture)
    }

}