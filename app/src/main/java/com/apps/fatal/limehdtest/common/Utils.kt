package com.apps.fatal.limehdtest.common

import com.apps.fatal.common_domain.BaseApplication
import com.apps.fatal.limehdtest.di.AppComponent

fun getAppInjector(): AppComponent {
    return BaseApplication.appComponent as AppComponent
}
