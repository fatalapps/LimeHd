package com.apps.fatal.data.db

import android.content.Context
import com.apps.fatal.data.common.DATABASE_NAME
import javax.inject.Inject

internal interface Database {
    fun getDatabase(): DatabaseRepository
    fun buildDatabase(): DatabaseRepository
}

internal class DatabaseImpl @Inject constructor(private val context: Context) : Database {
    private val databaseRepository
        get() = buildDatabase()


    override fun getDatabase() = databaseRepository

    override fun buildDatabase(): DatabaseRepository {
        return DatabaseRepositoryBuilder(context).build(DATABASE_NAME)
    }
}