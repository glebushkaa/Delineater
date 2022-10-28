package com.gleb.delineater.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gleb.delineater.data.room.dao.PictureDao
import com.gleb.delineater.data.room.entity.PictureEntity

@Database(entities = [PictureEntity::class], version = 1, exportSchema = false)
abstract class PictureDatabase : RoomDatabase() {

    abstract fun pictureDao(): PictureDao

}