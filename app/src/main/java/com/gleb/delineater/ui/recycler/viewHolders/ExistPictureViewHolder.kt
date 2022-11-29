package com.gleb.delineater.ui.recycler.viewHolders

import android.view.View
import com.bumptech.glide.Glide
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.ItemExistPictureBinding
import com.gleb.delineater.ui.extensions.hideFadeAnim
import com.gleb.delineater.ui.extensions.defaultFadeAnim
import com.gleb.delineater.ui.listeners.ExistPictureListener
import com.gleb.delineater.ui.listeners.getGlideProgressBarListener
import com.gleb.delineater.ui.recycler.BaseViewHolder
import java.io.File

private const val VISIBLE_DELETE_ALPHA = 0.9f

class ExistPictureViewHolder(private val view: View) :
    BaseViewHolder<ExistPictureListener, PictureEntity>(view) {

    override fun bind(listener: ExistPictureListener, item: PictureEntity) {
        val binding = ItemExistPictureBinding.bind(view)
        binding.apply {
            Glide.with(imageContainer)
                .load(File(item.picturePath))
                .listener(loadProgress.getGlideProgressBarListener())
                .into(imageContainer)

            deleteImageBtn.setOnLongClickListener {
                deleteImageBtn.hideFadeAnim()
                true
            }
            deleteImageBtn.setOnClickListener {
                listener.deletePicture(item)
            }
            root.setOnLongClickListener {
                deleteImageBtn.defaultFadeAnim(VISIBLE_DELETE_ALPHA)
                true
            }
            root.setOnClickListener {
                listener.openPicture(item)
            }
        }
    }
}