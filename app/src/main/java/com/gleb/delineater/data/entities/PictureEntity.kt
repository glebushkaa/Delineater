package com.gleb.delineater.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "picture_entity")
@Parcelize
class PictureEntity(
    @PrimaryKey(autoGenerate = true) val number: Long = 0,
    @ColumnInfo(name = "picture_path") var picturePath: String
) : Parcelable