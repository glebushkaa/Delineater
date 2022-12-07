package com.gleb.delineater.ui.recycler.viewHolders

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.ItemExistPictureBinding
import com.gleb.delineater.ui.listeners.ExistPictureListener
import com.gleb.delineater.ui.recycler.BaseViewHolder
import java.io.File

class ExistPictureViewHolder(private val view: View) :
    BaseViewHolder<ExistPictureListener, PictureEntity>(view) {

    private val visibileAlpha = 0.9f
    private val goneAlpha = 0f

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
                deleteImageBtn.showFideAnim()
                true
            }
            root.setOnClickListener {
                listener.openPicture(item)
            }
        }
    }

    private fun Button.showFideAnim() {
        this.visibility = View.VISIBLE
        this.animate().alpha(visibileAlpha).also {
            it.start()
        }
    }


    private fun Button.hideFadeAnim() {
        this.animate().alpha(goneAlpha).also {
            it.withEndAction {
                this.visibility = View.GONE
            }
            it.start()
        }
    }

    private fun ProgressBar.getGlideProgressBarListener() =
        object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                this@getGlideProgressBarListener.visibility = View.GONE
                return false
            }

        }
}