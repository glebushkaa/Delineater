package com.gleb.delineater.ui.recycler.diffUtil

import androidx.recyclerview.widget.DiffUtil

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