package com.apps.fatal.common_domain

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.math.ceil

inline fun <reified I> Any.takeIfIsInstance(void: (I) -> Unit): Unit? {
    val res: Unit? = try {
        if (this is I) void.invoke(this)
        else null
    } catch (e: Exception) {
        null
    }

    return res
}

fun ViewGroup.forEachChildTraversed(action: (View) -> Unit) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.forEachChildTraversed(action)
        }
        action(child)
    }
}

private var density = 0f
private fun getDensity(context: Context): Float {
    if (density == 0f) density = context.resources.displayMetrics.density
    return density
}

fun dp(dp: Float): Float {
    val density = getDensity(BaseApplication.appContext)
    return if (dp == 0f) 0f
    else ceil(density * dp)
}

fun color(@ColorRes res: Int) = ContextCompat.getColor(BaseApplication.appContext, res)
fun string(@StringRes res: Int) = BaseApplication.appContext.getString(res)
fun drawable(@DrawableRes res: Int) = if (res == 0) null else BaseApplication.appContext.getDrawable(res)