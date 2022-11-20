package com.gleb.delineater.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gleb.delineater.R
import com.gleb.delineater.ui.listeners.NewPictureListener
import com.gleb.delineater.ui.recycler.diffUtil.NewPictureDiffCallback
import com.gleb.delineater.ui.recycler.viewHolders.NewPictureViewHolder


class NewPictureAdapter(private val listener: NewPictureListener) :
    ListAdapter<Boolean, RecyclerView.ViewHolder>(NewPictureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NewPictureViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_new_picture, parent, false)
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewPictureViewHolder).bind(listener, currentList[position])
    }

    fun addItem(isItemExist: Boolean) {
        submitList(listOf(isItemExist))
    }

}