package com.apps.fatal.data.di

import com.apps.fatal.data.db.Database
import com.apps.fatal.data.db.DatabaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object DatabaseModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(database: Database): DatabaseRepository {
        return database.getDatabase()
    }
}