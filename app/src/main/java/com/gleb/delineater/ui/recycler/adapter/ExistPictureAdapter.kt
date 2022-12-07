package com.gleb.delineater.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.ui.listeners.ExistPictureListener
import com.gleb.delineater.ui.recycler.diffUtil.MenuPictureDiffCallback
import com.gleb.delineater.ui.recycler.viewHolders.ExistPictureViewHolder

class ExistPictureAdapter(private val listener: ExistPictureListener) :
    ListAdapter<PictureEntity, ViewHolder>(MenuPictureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExistPictureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_exist_picture, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ExistPictureViewHolder).bind(listener, currentList[position])
    }
}