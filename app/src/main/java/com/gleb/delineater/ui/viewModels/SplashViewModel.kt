package com.gleb.delineater.ui.viewModels

import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gleb.delineater.data.models.PictureModel
import com.gleb.delineater.data.room.dao.PictureDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SplashViewModel() : ViewModel() {

}