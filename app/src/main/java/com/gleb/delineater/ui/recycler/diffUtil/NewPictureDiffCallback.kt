package com.gleb.delineater.ui.recycler.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.gleb.delineater.data.entities.PictureEntity

class NewPictureDiffCallback : DiffUtil.ItemCallback<Boolean>() {

    override fun areItemsTheSame(
        oldItem: Boolean,
        newItem: Boolean
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: Boolean,
        newItem: Boolean
    ) = oldItem == newItem
}