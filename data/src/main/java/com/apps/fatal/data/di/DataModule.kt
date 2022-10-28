package com.apps.fatal.data.di

import dagger.Module

@Module(includes = [RepositoryModuleInternal::class, RetrofitModule::class, DatabaseModule::class])
interface DataModule