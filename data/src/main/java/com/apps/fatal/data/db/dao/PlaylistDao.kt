package com.apps.fatal.data.db.dao

import androidx.room.*
import com.apps.fatal.data.models.ChannelDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface PlaylistDao {

    @Query("SELECT * FROM ChannelDataEntity WHERE isFavourite = 1")
    fun getFavouriteFlow(): Flow<List<ChannelDataEntity>>

    @Query("SELECT * FROM ChannelDataEntity")
    fun getFlow(): Flow<List<ChannelDataEntity>>

    @Query("SELECT * FROM ChannelDataEntity WHERE id = :id")
    fun getChannelFlow(id: Int): Flow<ChannelDataEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ch: ChannelDataEntity): Long

    @Update
    suspend fun update(ch: ChannelDataEntity)

    @Query("SELECT * FROM ChannelDataEntity WHERE id = :id")
    suspend fun getById(id: Int): ChannelDataEntity?

    @Transaction
    suspend fun setIsFavourite(id: Int, state: Boolean) {
        val e = getById(id)
        e?.let {
            val newEntity = it.apply { isFavourite = state }
            update(newEntity)
        }
    }

    //to retain isFavourite value
    @Transaction
    suspend fun upsertList(list: List<ChannelDataEntity>) {
        list.forEach { ch ->
            val rowExists = getById(ch.id)
            if (rowExists != null) update(ch.apply {
                isFavourite = rowExists.isFavourite
            })
            else insert(ch)
        }
    }
}