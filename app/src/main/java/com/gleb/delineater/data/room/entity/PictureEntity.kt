package com.gleb.delineater.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_entity")
class PictureEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "picture_path") val picturePath: String?
)