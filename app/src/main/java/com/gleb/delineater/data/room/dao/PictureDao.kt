package com.gleb.delineater.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gleb.delineater.MyApp.Companion.TABLE_NAME
import com.gleb.delineater.data.room.entity.PictureEntity

@Dao
interface PictureDao {

    @Query("SELECT * FROM picture_entity")
    suspend fun getAllPictures(): List<PictureEntity>

    @Insert
    fun addPicture(pictureEntity: PictureEntity)

}