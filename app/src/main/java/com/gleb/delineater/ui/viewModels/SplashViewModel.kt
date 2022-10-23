package com.gleb.delineater.ui.viewModels

import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.models.PictureModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SplashViewModel : ViewModel() {

    suspend fun fillPictureList(drawable: BitmapDrawable) = suspendCoroutine<List<PictureModel>>{ emitter ->
        viewModelScope.launch(Dispatchers.IO) {
            val pictureList = arrayListOf<PictureModel>()
            for (i in 1..10) {
                pictureList.add(PictureModel(drawable.bitmap))
            }
            emitter.resume(pictureList)
        }
    }

}