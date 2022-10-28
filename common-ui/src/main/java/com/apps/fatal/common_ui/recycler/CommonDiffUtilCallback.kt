package com.apps.fatal.common_ui.recycler

import androidx.recyclerview.widget.DiffUtil
import com.apps.fatal.common_ui.Entry

class CommonDiffUtilCallback<in T : Entry<*>>(private val oldItems: List<T>, private val newItems: List<T>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.diffUtilCallback.isSameItem(newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.diffUtilCallback.isSameContent(newItem)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.diffUtilCallback.getPayload(newItem)
    }
}