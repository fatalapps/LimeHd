package com.apps.fatal.common_ui

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.apps.fatal.common_domain.forEachChildTraversed

private var ids = 0L
private var counter = 5000
private val viewTypes = HashMap<String, Int>()

val viewTypesPublic
    get() = viewTypes.toMap()

private val viewFactories = HashMap<Int, (parent: ViewGroup) -> BaseViewHolder>()

internal fun register(id: String, factory: (parent: ViewGroup) -> BaseViewHolder) {
    if (!viewTypes.containsKey(id)) {
        val value = ++counter
        viewTypes[id] = value
        viewFactories[value] = factory
    }
}

fun createHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    return viewFactories[viewType]?.invoke(parent) ?: throw IllegalStateException("Cannot create view holder")
}

fun bindHolder(holder: RecyclerView.ViewHolder, item: Entry<*>?) {
    (holder as? BaseViewHolder)?.run {
        bind(item)
        checkEnabled(item)
    }
}

abstract class Entry<T : BaseViewHolder> {
    var tag: String? = null
    var enabled = true
    var stableId = ++ids

    open val diffUtilCallback = DiffUtilCallback()

    companion object {

        inline fun <reified T : Entry<*>> getViewType() = viewTypesPublic[T::class.java.name]
    }

    val viewType: Int
        get() {
            if (viewTypes[this.javaClass.name] == null) {
                register(this.javaClass.name, ::createViewHolder)
            }
            return viewTypes[javaClass.name] ?: throw IllegalStateException("Invalid view type")
        }

    abstract fun createViewHolder(parent: ViewGroup): T

    open class DiffUtilCallback {
        open fun isSameItem(other: Entry<*>): Boolean {
            return false
        }

        open fun isSameContent(other: Entry<*>): Boolean {
            return false
        }

        open fun getPayload(other: Entry<*>): Any? {
            return null
        }
    }
}

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(entry: Entry<*>?)

    @CallSuper
    open fun checkEnabled(entry: Entry<*>?) {
        when (itemView) {
            is ViewGroup -> (itemView as? ViewGroup)?.forEachChildTraversed { it.isEnabled = entry?.enabled ?: true }
            else -> itemView.isEnabled = entry?.enabled ?: true
        }
    }
}