package com.gleb.delineater.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.gleb.delineater.data.entities.PictureEntity
import com.gleb.delineater.databinding.ItemExistPictureBinding
import com.gleb.delineater.databinding.ItemNewPictureBinding
import com.gleb.delineater.ui.listeners.MenuPictureListener
import com.gleb.delineater.ui.listeners.getGlideProgressBarListener
import com.gleb.delineater.ui.extensions.hideWithFadeAnimation
import com.gleb.delineater.ui.extensions.showWithFadeAnimation
import com.gleb.delineater.ui.recycler.diffUtil.MenuPictureDiffUtil
import java.io.File

private const val PICTURE_ITEM = 0
private const val ADD_PICTURE_ITEM = 1
private const val VISIBLE_DELETE_ALPHA = 0.9f

class MenuPictureAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val oldPictureList = arrayListOf<PictureEntity>()
    private var menuPictureListener: MenuPictureListener? = null

    override fun getItemViewType(position: Int) =
        oldPictureList[position].picturePath?.let {
            PICTURE_ITEM
        } ?: run {
            ADD_PICTURE_ITEM
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            PICTURE_ITEM -> ExistPictureViewHolder(
                ItemExistPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> NewPictureViewHolder(
                ItemNewPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == PICTURE_ITEM) {
            (holder as ExistPictureViewHolder).bind()
        } else {
            (holder as NewPictureViewHolder).bind()
        }
    }

    override fun getItemCount() = oldPictureList.size

    fun setOnItemClickedListener(listener: MenuPictureListener) {
        menuPictureListener = listener
    }

    fun setData(newPictureList: List<PictureEntity>) {
        val diffUtil = MenuPictureDiffUtil(oldPictureList, newPictureList)
        val diffResults = DiffUtil.calculateDiff(diffUtil,true)
        oldPictureList.clear()
        oldPictureList.addAll(newPictureList)
        oldPictureList.add(PictureEntity(picturePath = null))
        diffResults.dispatchUpdatesTo(this)
    }

    inner class ExistPictureViewHolder(private val binding: ItemExistPictureBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            val pictureEntity = oldPictureList[bindingAdapterPosition]

            binding.apply {
                Glide.with(imageContainer)
                    .load(File(pictureEntity.picturePath.orEmpty()))
                    .listener(progressBar.getGlideProgressBarListener())
                    .into(imageContainer)

                deleteImageBtn.setOnLongClickListener {
                    deleteImageBtn.hideWithFadeAnimation()
                    true
                }
                deleteImageBtn.setOnClickListener {
                    menuPictureListener?.deletePicture(pictureEntity)
                }
                root.setOnLongClickListener {
                    deleteImageBtn.showWithFadeAnimation(VISIBLE_DELETE_ALPHA)
                    true
                }
                root.setOnClickListener {
                    menuPictureListener?.openExistPicture(pictureEntity)
                }
            }
        }
    }

    inner class NewPictureViewHolder(private val binding: ItemNewPictureBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                menuPictureListener?.openNewPicture()
            }
        }
    }

}