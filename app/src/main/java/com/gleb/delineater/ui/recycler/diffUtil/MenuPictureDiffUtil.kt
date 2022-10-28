package com.gleb.delineater.ui.recycler.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.gleb.delineater.data.models.PictureModel

class MenuPictureDiffUtil(
    private val oldPictureList: List<PictureModel>,
    private val newPictureList: List<PictureModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldPictureList.size

    override fun getNewListSize() = newPictureList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPictureList[oldItemPosition] == newPictureList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldPictureList[oldItemPosition].picture == newPictureList[newItemPosition].picture
}