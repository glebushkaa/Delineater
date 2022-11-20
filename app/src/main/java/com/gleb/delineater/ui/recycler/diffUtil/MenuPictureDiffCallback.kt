package com.gleb.delineater.ui.recycler.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.gleb.delineater.data.entities.PictureEntity

class MenuPictureDiffCallback : DiffUtil.ItemCallback<PictureEntity>() {

    override fun areItemsTheSame(
        oldItem: PictureEntity,
        newItem: PictureEntity
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: PictureEntity,
        newItem: PictureEntity
    ) = oldItem.picturePath == newItem.picturePath
}