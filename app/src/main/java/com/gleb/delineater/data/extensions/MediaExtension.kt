package com.gleb.delineater.data.extensions

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

private const val albumName = "PictureAlbum"
private const val jpgType = ".jpg"
private const val jpgMimeType = "image/jpg"

fun Bitmap.saveAlbumImage(callback: (String) -> Unit) {
    val file = getPictureDir(albumName)

    if (!file.exists() && !file.mkdirs()) {
        file.mkdir()
    }
    val uuid = UUID.randomUUID().toString()
    val name = "picture$uuid$jpgType"
    val fileName = file.absolutePath + "/" + name
    val newFile = File(fileName)

    compress(Bitmap.CompressFormat.JPEG, 100, newFile.outputStream())
    callback(fileName)
}

private fun getPictureDir(fileName: String): File {
    val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    return File(file, fileName)
}

fun Context.saveGalleryPicture(bitmap: Bitmap) {
    val uuid = UUID.randomUUID().toString()
    val filename = uuid + jpgType
    val fos: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentResolver.let { resolver ->
            val contentValues = getContentValues(filename)
            val imageUri: Uri? = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
            )
            imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        FileOutputStream(getPictureDir(filename))
    }
    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
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

fun Context.decodePictureFile(picturePath: String, completeCallback: (BitmapDrawable) -> Unit) {
    val file = File(picturePath)
    if (file.exists()) {
        val picture = BitmapFactory.decodeFile(picturePath).toDrawable(resources)
        completeCallback(picture)
    } else {
        return
    }
}

