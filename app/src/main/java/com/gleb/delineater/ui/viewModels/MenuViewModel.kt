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

    fun getAllPictures() {
        viewModelScope.launch(Dispatchers.IO) {
            pictureLiveData.postValue(pictureRepository.getAllPictures())
        }
    }

}