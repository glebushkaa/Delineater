package com.gleb.delineater.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MenuViewModel(private val pictureRepository: PictureRepository) : ViewModel() {

    val pictureLiveData = MutableLiveData<List<PictureEntity>>()
    private val pictureList = arrayListOf<PictureEntity>()

    fun getAllPictures() {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.getAllPictures().let {
                pictureLiveData.postValue(it)
                pictureList.addAll(it)
            }
        }
    }

    fun deleteImage(pictureEntity: PictureEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            pictureRepository.deletePicture(pictureEntity)
            pictureList.remove(pictureEntity)
            pictureLiveData.postValue(pictureList)
        }
    }
}