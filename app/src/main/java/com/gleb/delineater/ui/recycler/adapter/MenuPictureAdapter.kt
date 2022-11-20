package com.gleb.delineater.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gleb.delineater.R
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.ui.listeners.MenuPictureListener
import com.gleb.delineater.ui.recycler.diffUtil.MenuPictureDiffCallback
import com.gleb.delineater.ui.recycler.viewHolders.ExistPictureViewHolder
import com.gleb.delineater.ui.recycler.viewHolders.NewPictureViewHolder

private const val PICTURE_ITEM = 0
private const val ADD_PICTURE_ITEM = 1

class MenuPictureAdapter : ListAdapter<PictureEntity, ViewHolder>(MenuPictureDiffCallback()) {

    private var menuPictureListener: MenuPictureListener? = null

    override fun getItemViewType(position: Int) =
        currentList[position].picturePath?.let {
            PICTURE_ITEM
        } ?: run {
            ADD_PICTURE_ITEM
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            PICTURE_ITEM -> ExistPictureViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_exist_picture, parent, false)
            )
            else -> NewPictureViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_new_picture, parent, false)
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == PICTURE_ITEM) {
            (holder as ExistPictureViewHolder).bind(menuPictureListener, currentList[position])
        } else {
            (holder as NewPictureViewHolder).bind(menuPictureListener, currentList[position])
        }
    }

    override fun getItemCount() = currentList.size

    fun setOnItemClickedListener(listener: MenuPictureListener) {
        menuPictureListener = listener
    }

}