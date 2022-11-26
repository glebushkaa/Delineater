package com.gleb.delineater.ui.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.ui.types.ColorPickerType
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawViewModel(private val pictureRepository: PictureRepository) : ViewModel() {

    var currentPicture: PictureEntity? = null
    var colorPickerType: ColorPickerType = ColorPickerType.BrushColorPicker

    fun addCurrentPicture(picturePath: String) {
        currentPicture?.let {
            updatePicture(PictureEntity(uid = it.uid, picturePath = picturePath))
            Log.d("Update picture", it.picturePath)
            Log.d("Update picture new", picturePath)
        } ?: run {
            addNewPicture(PictureEntity(picturePath = picturePath))
            Log.d("New add picture:", picturePath)
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