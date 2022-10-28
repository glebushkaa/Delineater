package com.gleb.delineater.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.gleb.delineater.listeners.MenuPictureListener
import com.gleb.delineater.data.models.PictureModel
import com.gleb.delineater.databinding.ItemAddPictureBinding
import com.gleb.delineater.databinding.ItemMenuBinding
import com.gleb.delineater.ui.recycler.diffUtil.MenuPictureDiffUtil

class MenuPictureAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val oldPictureList = arrayListOf<PictureModel>()
    private var menuPictureListener: MenuPictureListener? = null

    companion object {
        const val PICTURE_ITEM = 0
        const val ADD_PICTURE_ITEM = 1
    }

    override fun getItemViewType(position: Int) =
        if (oldPictureList[position].picture != null) {
            PICTURE_ITEM
        } else {
            ADD_PICTURE_ITEM
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            PICTURE_ITEM -> PictureViewHolder(
                ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> AddPictureViewHolder(
                ItemAddPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == PICTURE_ITEM) {
            (holder as PictureViewHolder).bind()
        } else {
            (holder as AddPictureViewHolder).bind()
        }
    }

    override fun getItemCount() = oldPictureList.size

    fun setOnItemClickedListener(listener: MenuPictureListener) {
        menuPictureListener = listener
    }

    fun setData(newPictureList: List<PictureModel>) {
        val diffUtil = MenuPictureDiffUtil(oldPictureList, newPictureList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldPictureList.clear()
        oldPictureList.addAll(newPictureList)
        oldPictureList.add(PictureModel(picture = null))
        diffResults.dispatchUpdatesTo(this)
    }

    inner class PictureViewHolder(private val binding: ItemMenuBinding) : ViewHolder(binding.root) {
        fun bind() {
            Glide.with(binding.imageContainer)
                .load(oldPictureList[bindingAdapterPosition].picture)
                .into(binding.imageContainer)
            binding.root.setOnClickListener {
                oldPictureList[bindingAdapterPosition].picture?.let { bitmap ->
                    menuPictureListener?.showPictureInfo(
                        "Just picture rn",
                        bitmap
                    )
                }
            }
        }
    }

    inner class AddPictureViewHolder(private val binding: ItemAddPictureBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                menuPictureListener?.showAddPictureInfo("Would be functionality to add a new drawing surface")
            }
        }
    }

}