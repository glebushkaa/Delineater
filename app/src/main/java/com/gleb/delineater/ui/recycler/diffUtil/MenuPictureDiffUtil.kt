package com.gleb.delineater.ui.recycler.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.gleb.delineater.data.entities.PictureEntity

class MenuPictureDiffUtil(
    private val oldPictureList: List<PictureEntity>,
    private val newPictureList: List<PictureEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldPictureList.size

    override fun getNewListSize() = newPictureList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPictureList[oldItemPosition] == newPictureList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPictureList[oldItemPosition].picturePath == newPictureList[newItemPosition].picturePath
}