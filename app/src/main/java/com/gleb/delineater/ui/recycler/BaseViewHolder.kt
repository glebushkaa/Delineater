package com.gleb.delineater.ui.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder<T,E>(view: View) : ViewHolder(view) {
    abstract fun bind(listener: T, item: E)
}