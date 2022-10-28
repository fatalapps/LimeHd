package com.apps.fatal.common_ui

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView?.loadWithGlide(url: String) {
    this?.let {
        Glide.with(context)
            .load(url)
            .into(it)
    }
}