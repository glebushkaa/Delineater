package com.gleb.delineater.domain.usecases

import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepositoryImpl

class NewPictureUseCase(private val pictureRepository: PictureRepositoryImpl) {

    suspend fun addNewPicture(picture: PictureEntity) = pictureRepository.addNewPicture(picture)

}