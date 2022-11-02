package com.gleb.delineater.data

import android.graphics.Bitmap
import android.os.Environment
import com.gleb.delineater.data.entities.PaintEntity
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class FileHelper {

    fun saveImage(bitmap: Bitmap, callback: (String) -> Unit) {
        val file = getPictureDisc()

        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }

        val uuid = UUID.randomUUID().toString()
        val name = "picture$uuid.jpg"
        val fileName = file.absolutePath + "/" + name
        val newFile = File(fileName)

        try {
            val fileOutPutStream = FileOutputStream(newFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutPutStream)
            fileOutPutStream.flush()
            fileOutPutStream.close()
            callback(fileName)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun savePaint(paintList: List<PaintEntity>, callback: (String) -> Unit) {
        val file = getPaintDisc()

        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }

        val uuid = UUID.randomUUID().toString()
        val name = "paint$uuid"
        val fileName = file.absolutePath + "/" + name
        val newFile = File(fileName)

        try {
            val fileOutPutStream = FileOutputStream(newFile)
            fileOutPutStream.flush()
            fileOutPutStream.close()
            callback(fileName)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getPictureDisc(): File {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file, "PictureAlbum")
    }

    private fun getPaintDisc(): File {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file, "PaintAlbum")
    }

}