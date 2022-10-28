package com.apps.fatal.common_domain

import android.app.Activity
import androidx.fragment.app.Fragment

interface BaseAppComponent {
    fun inject(activity: Activity)
    fun inject(fragment: Fragment)
}