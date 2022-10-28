package com.apps.fatal.limehdtest

import com.apps.fatal.common_domain.BaseApplication
import com.apps.fatal.limehdtest.di.DaggerAppComponent

class App: BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}