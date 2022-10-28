package com.apps.fatal.common_ui.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apps.fatal.common_ui.Entry
import com.apps.fatal.common_ui.bindHolder
import com.apps.fatal.common_ui.createHolder

open class BaseRecyclerAdapter(val data: MutableList<Entry<*>> = arrayListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        createHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        bindHolder(holder, data[position])

    override fun getItemViewType(position: Int) = data[position].viewType
    override fun getItemCount() = data.size

    /**
     * Override [Entry.diffUtilCallback] before use.
     */
    fun submitList(list: List<Entry<*>>) {
        val callback = CommonDiffUtilCallback(data, list)
        val result = DiffUtil.calculateDiff(callback)
        data.clear()
        data.addAll(list)
        result.dispatchUpdatesTo(this)
    }
}