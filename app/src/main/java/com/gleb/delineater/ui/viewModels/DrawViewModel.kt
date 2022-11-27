package com.gleb.delineater.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.ui.types.ColorPickerType
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawViewModel(private val pictureRepository: PictureRepositoryImpl) : ViewModel() {

    var currentPicture: PictureEntity? = null
    var isNewPicture = true
    var colorPickerType: ColorPickerType = ColorPickerType.BrushColorPicker

    fun addCurrentPicture(picturePath: String) {
        currentPicture?.let {
            updatePicture(PictureEntity(number = it.number, picturePath = picturePath))
        } ?: run {
            addNewPicture(PictureEntity(picturePath = picturePath))
        }
        currentPicture = PictureEntity(picturePath = picturePath)
    }

    private fun addNewPicture(picture: PictureEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.addNewPicture(picture)
        }
    }

    private fun updatePicture(picture: PictureEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.updatePicture(picture)
        }
    }

}