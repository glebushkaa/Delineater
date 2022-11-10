package com.gleb.delineater.ui.viewModels

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.gleb.delineater.data.saveGalleryPicture

class DownloadViewModel: ViewModel() {

    var picturePath : String? = null

    fun saveGalleryPicture(bitmap: Bitmap,contentResolver: ContentResolver){
        bitmap.saveGalleryPicture(contentResolver)
    }

}