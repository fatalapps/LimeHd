package com.apps.fatal.data.di

import com.apps.fatal.app_domain.repositories.PlaylistRepository
import com.apps.fatal.data.db.Database
import com.apps.fatal.data.db.DatabaseImpl
import com.apps.fatal.data.repositoryimpl.PlaylistRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class RepositoryModuleInternal {

    @Singleton
    @Binds
    abstract fun database(database: DatabaseImpl): Database

    @Singleton
    @Binds
    abstract fun playlistRepo(playlistRepo: PlaylistRepositoryImpl): PlaylistRepository
}