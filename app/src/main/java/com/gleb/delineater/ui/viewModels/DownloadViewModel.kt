package com.gleb.delineater.ui.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.extensions.saveGalleryPicture
import com.gleb.delineater.data.repositories.PictureRepository
import com.gleb.delineater.domain.usecases.newPicture.NewPictureUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadViewModel(private val newPictureUseCase: NewPictureUseCase) : ViewModel() {

    var currentPicture: PictureEntity? = null

    fun saveGalleryPicture(bitmap: Bitmap, context: Context) {
        context.saveGalleryPicture(bitmap)
    }


    fun addCurrentPicture(picturePath: String) {
        currentPicture?.let {
            updatePicture(PictureEntity(uid = it.uid, picturePath = picturePath))
        } ?: run {
            addNewPicture(PictureEntity(picturePath = picturePath))
        }
        currentPicture = PictureEntity(picturePath = picturePath)
    }

    private fun addNewPicture(picture: PictureEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            newPictureUseCase.addNewPicture(picture)
        }
    }

    private fun updatePicture(picture: PictureEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.updatePicture(picture)
        }
    }

}