package com.apps.fatal.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apps.fatal.data.db.dao.PlaylistDao
import com.apps.fatal.data.models.ChannelDataEntity

@Database(
    entities = [ChannelDataEntity::class],
    version = DatabaseRepository.DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(Converters::class)
internal abstract class DatabaseRepository : RoomDatabase() {

    companion object {

        const val DATABASE_VERSION = 1
    }

    abstract fun getPlaylistDao(): PlaylistDao

    fun nukeAll() {
        clearAllTables()
    }
}

@Suppress("PrivatePropertyName")
internal class DatabaseRepositoryBuilder(private val context: Context) {

    fun build(databaseName: String): DatabaseRepository {
        return Room.databaseBuilder(context, DatabaseRepository::class.java, databaseName)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }
}