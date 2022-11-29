package com.gleb.delineater.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.ui.types.ColorPickerType
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepositoryImpl
import com.gleb.delineater.domain.usecases.NewPictureUseCase
import com.gleb.delineater.domain.usecases.UpdatePictureUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawViewModel(
    private val updatePictureUseCase: UpdatePictureUseCase,
    private val newPictureUseCase: NewPictureUseCase
) : ViewModel() {

    var currentPicture: PictureEntity? = null
    var isNewPicture = true
    var colorPickerType: ColorPickerType = ColorPickerType.BrushColorPicker

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

    fun setNewPicturePath(picturePath: String) {
        if (isNewPicture) {
            currentPicture = PictureEntity(picturePath = picturePath)
        } else {
            currentPicture?.picturePath = picturePath
        }
    }

}