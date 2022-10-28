package com.apps.fatal.common_domain

import android.app.Application
import android.content.Context

abstract class BaseApplication : Application() {

    init {
        appContext = this
    }

    companion object {
        lateinit var appComponent: BaseAppComponent
        lateinit var appContext: Context
    }
}