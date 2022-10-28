package com.apps.fatal.limehdtest.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.apps.fatal.common_ui.mvvm.ViewModelKey
import com.apps.fatal.limehdtest.ui.viewmodels.ChannelsListViewModel
import com.apps.fatal.limehdtest.ui.viewmodels.MainPagerViewModel
import com.apps.fatal.limehdtest.ui.viewmodels.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChannelsListViewModel::class)
    internal abstract fun channelsViewModel(viewModel: ChannelsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainPagerViewModel::class)
    internal abstract fun pagerViewModel(viewModel: MainPagerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    internal abstract fun playerViewModel(viewModel: PlayerViewModel): ViewModel
}