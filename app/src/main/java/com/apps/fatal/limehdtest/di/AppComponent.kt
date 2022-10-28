package com.apps.fatal.limehdtest.di

import android.app.Application
import com.apps.fatal.common_domain.BaseAppComponent
import com.apps.fatal.data.di.DataModule
import com.apps.fatal.limehdtest.MainActivity
import com.apps.fatal.limehdtest.ui.MainPagerFragment
import com.apps.fatal.limehdtest.ui.fragments.ChannelsListFragment
import com.apps.fatal.limehdtest.ui.fragments.PlayerFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        ViewModelsModule::class
    ]
)
interface AppComponent: BaseAppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(fr: ChannelsListFragment)
    fun inject(fr: MainPagerFragment)
    fun inject(fr: PlayerFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}