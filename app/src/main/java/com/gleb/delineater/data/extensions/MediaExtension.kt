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
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val cacheAlbumName = "PictureAlbum"
private const val jpgType = ".jpg"
private const val jpgMimeType = "image/jpg"

suspend fun Bitmap.cachePicture() = suspendCoroutine<String> { continuation ->
    runCatching {
        val file = getPictureDir(cacheAlbumName)

        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }
        val uuid = UUID.randomUUID().toString()
        val name = "picture$uuid$jpgType"
        val filePath = file.absolutePath + "/" + name
        val newFile = File(filePath)

        compress(Bitmap.CompressFormat.JPEG, 100, newFile.outputStream())
        continuation.resume(filePath)
    }.recover {
        continuation.resumeWithException(it)
    }
}

private fun getPictureDir(fileName: String) =
    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)

fun Context.saveGalleryPicture(bitmap: Bitmap) = runCatching {
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

suspend fun Context.decodePictureFile(picturePath: String) =
    suspendCoroutine<BitmapDrawable> { continuation ->
        val file = File(picturePath)
        if (file.exists()) {
            continuation.resume(BitmapFactory.decodeFile(picturePath).toDrawable(resources))
        } else {
            continuation.resumeWithException(FileNotFoundException())
        }
    }

