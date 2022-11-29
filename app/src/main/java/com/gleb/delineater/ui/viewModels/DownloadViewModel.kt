package com.gleb.delineater.ui.viewModels

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.extensions.saveGalleryPicture
import com.gleb.delineater.domain.usecases.NewPictureUseCase
import com.gleb.delineater.domain.usecases.UpdatePictureUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val newPictureUseCase: NewPictureUseCase,
    private val updatePictureUseCase: UpdatePictureUseCase
) : ViewModel() {

    var currentPicture: PictureEntity? = null
    var isNewPicture = true

    fun saveGalleryPicture(bitmap: Bitmap, context: Context) {
        context.saveGalleryPicture(bitmap)
    }

    fun addCurrentPicture() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNewPicture) {
                currentPicture?.let {
                    val number = newPictureUseCase.addNewPicture(it)
                    currentPicture = PictureEntity(number = number, picturePath = it.picturePath)
                }
            } else {
                currentPicture?.let { updatePictureUseCase.updatePicture(it) }
            }
        }
    }
}