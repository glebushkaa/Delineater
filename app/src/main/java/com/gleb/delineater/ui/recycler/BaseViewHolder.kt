package com.gleb.delineater.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gleb.delineater.ui.listeners.BaseAdapterListener

abstract class BaseViewHolder<T : BaseAdapterListener,E>(view: View) : ViewHolder(view) {
    abstract fun bind(listener: T, item: E)
}