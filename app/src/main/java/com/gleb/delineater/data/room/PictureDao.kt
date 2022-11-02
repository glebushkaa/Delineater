package com.gleb.delineater.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gleb.delineater.data.entities.PictureEntity

@Dao
interface PictureDao {

    @Query("SELECT * FROM picture_entity")
    suspend fun getAllPictures(): List<PictureEntity>

    @Insert
    suspend fun addPicture(pictureEntity: PictureEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePicture(pictureEntity: PictureEntity)

}