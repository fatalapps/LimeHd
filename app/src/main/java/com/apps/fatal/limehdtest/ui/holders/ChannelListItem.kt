package com.apps.fatal.limehdtest.ui.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.common_domain.color
import com.apps.fatal.common_domain.takeIfIsInstance
import com.apps.fatal.common_ui.BaseViewHolder
import com.apps.fatal.common_ui.Entry
import com.apps.fatal.common_ui.loadWithGlide
import com.apps.fatal.limehdtest.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class ChannelListItem(val channel: ChannelEntity, val callback: Callback) : Entry<ChannelItemViewHolder>() {

    override fun createViewHolder(parent: ViewGroup) = ChannelItemViewHolder.inflate(parent)

    override val diffUtilCallback = object : DiffUtilCallback() {

        override fun isSameItem(other: Entry<*>): Boolean {
            var same = false
            other.takeIfIsInstance<ChannelListItem> {
                same = channel.id == it.channel.id
            }
            return same
        }

        override fun isSameContent(other: Entry<*>): Boolean {
            var same = false
            other.takeIfIsInstance<ChannelListItem> {
                val otherD = it.channel
                same = channel.nameRu == channel.nameRu && channel.image == channel.image
                        && channel.url == channel.url && channel.urlSound == channel.urlSound
                        && channel.current == otherD.current && channel.isFavourite == otherD.isFavourite
            }
            return same
        }
    }

    interface Callback {
        fun onClick(id: Int)
        fun onFavourite(id: Int, state: Boolean)
    }
}

class ChannelItemViewHolder(parent: View) : BaseViewHolder(parent) {

    companion object {

        fun inflate(parent: ViewGroup) = ChannelItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.channel_list_item_holder,
                parent,
                false
            )
        )
    }

    private val icon: ImageView = itemView.findViewById(R.id.icon)
    private val title: MaterialTextView = itemView.findViewById(R.id.title)
    private val subtitle: MaterialTextView = itemView.findViewById(R.id.subtitle)
    private val favourite: ImageView = itemView.findViewById(R.id.favourite)
    private val cardView: MaterialCardView = itemView.findViewById(R.id.container)

    override fun bind(entry: Entry<*>?) {
        if (entry !is ChannelListItem) return

        icon.setImageDrawable(null)

        val ch = entry.channel

        setIsFavourite(ch.isFavourite)

        title.text = ch.nameRu
        subtitle.text = ch.current.title

        ch.image?.let { icon.loadWithGlide(it) }

        cardView.setOnClickListener {
            entry.callback.onClick(ch.id)
        }

        favourite.setOnClickListener {
            val state = ch.isFavourite.not()
            setIsFavourite(state)
            entry.callback.onFavourite(ch.id, state)
        }
    }

    private fun setIsFavourite(state: Boolean) {
        favourite.drawable?.let {
            val d = it
            d.setTint(color(if (state) R.color.primary_blue else R.color.favouriteInactive))
            favourite.setImageDrawable(d)
        }
    }

}