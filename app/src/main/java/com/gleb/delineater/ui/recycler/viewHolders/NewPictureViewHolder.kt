package com.gleb.delineater.ui.recycler.viewHolders

import android.view.View
import com.gleb.delineater.databinding.ItemNewPictureBinding
import com.gleb.delineater.ui.listeners.NewPictureListener
import com.gleb.delineater.ui.recycler.BaseViewHolder

class NewPictureViewHolder(private val view: View) :
    BaseViewHolder<NewPictureListener, Boolean>(view) {

    override fun bind(listener: NewPictureListener, item: Boolean) {
        val binding = ItemNewPictureBinding.bind(view)
        binding.root.setOnClickListener {
            listener.openNewPicture()
        }
    }
}