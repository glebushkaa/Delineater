package com.gleb.delineater.ui.viewModels

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Picture
import androidx.lifecycle.ViewModel
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.data.extensions.saveGalleryPicture

class DownloadViewModel : ViewModel() {

    var pictureEntity: PictureEntity? = null

    fun saveGalleryPicture(bitmap: Bitmap, context: Context) {
        context.saveGalleryPicture(bitmap)
    }

}