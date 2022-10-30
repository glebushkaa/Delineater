package com.gleb.delineater.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.room.PictureDao
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.repositories.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(private val pictureRepository: PictureRepository) : ViewModel() {

    val picturesLiveData = MutableLiveData<List<PictureEntity>>()

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            picturesLiveData.postValue(
                pictureRepository.getAllPictures()
            )
        }
    }

}