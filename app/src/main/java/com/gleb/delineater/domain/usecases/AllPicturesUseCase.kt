package com.gleb.delineater.domain.usecases

import com.gleb.delineater.data.repositories.PictureRepositoryImpl

class AllPicturesUseCase(
    private val pictureRepository: PictureRepositoryImpl
) {

    suspend fun getAllPictures() = pictureRepository.getAllPictures()

}