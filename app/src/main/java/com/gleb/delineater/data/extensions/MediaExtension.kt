package com.gleb.delineater.data.extensions

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.gleb.delineater.ui.extensions.showToast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

private const val albumName = "PictureAlbum"
private const val jpgType = ".jpg"
private const val jpgMimeType = "image/jpg"

fun Bitmap.saveAlbumImage(callback: (String) -> Unit) {
    val file = getPictureDisc()

    if (!file.exists() && !file.mkdirs()) {
        file.mkdir()
    }

    val uuid = UUID.randomUUID().toString()
    val name = "picture$uuid$jpgType"
    val fileName = file.absolutePath + "/" + name
    val newFile = File(fileName)

    try {
        val a = newFile.outputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, a)
        callback(fileName)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun getPictureDisc(): File {
    val file = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    )
    return File(file, albumName)
}

fun Bitmap.saveGalleryPicture(contentResolver: ContentResolver) {
    val uuid = UUID.randomUUID().toString()
    val filename = uuid + jpgType
    var fos: OutputStream?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentResolver.also { resolver ->
            val contentValues = getContentValues(filename)
            val imageUri: Uri? = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            )
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }
    fos?.use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

}

@RequiresApi(Build.VERSION_CODES.Q)
private fun getContentValues(fileName: String) = ContentValues().apply {
    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
    put(MediaStore.MediaColumns.MIME_TYPE, jpgMimeType)
    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
}

fun deletePictureFile(filePath: String) {
    val file = File(filePath)
    if (file.exists()) {
        file.delete()
    }
}
