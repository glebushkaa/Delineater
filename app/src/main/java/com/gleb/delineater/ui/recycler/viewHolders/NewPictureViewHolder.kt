package com.gleb.delineater.ui.recycler.viewHolders

import android.view.View
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.ItemNewPictureBinding
import com.gleb.delineater.ui.listeners.MenuPictureListener
import com.gleb.delineater.ui.recycler.BaseViewHolder

class NewPictureViewHolder(private val view: View) : BaseViewHolder<MenuPictureListener, PictureEntity>(view) {
    override fun bind(listener: MenuPictureListener, item: PictureEntity) {
        val binding = ItemNewPictureBinding.bind(view)
        binding.root.setOnClickListener {
            listener.openNewPicture()
        }
    }
}