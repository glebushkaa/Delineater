package com.gleb.delineater.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.ColorPickerType
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.parcelableCreator

class DrawViewModel(private val pictureRepository: PictureRepository) : ViewModel() {

    var currentPicture: PictureEntity? = null
    var colorPickerType: ColorPickerType = ColorPickerType.BrushColorPicker

    fun addCurrentPicture(picturePath: String) {
        currentPicture?.let {
            updatePicture(PictureEntity(uid = it.uid, picturePath = picturePath))
        } ?: run {
            addNewPicture(PictureEntity(picturePath = picturePath))
        }
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