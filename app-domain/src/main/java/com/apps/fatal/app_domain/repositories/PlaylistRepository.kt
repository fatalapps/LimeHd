package com.apps.fatal.app_domain.repositories

import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.app_domain.repositories.entities.ChannelsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface PlaylistRepository {

    suspend fun getChannels(): ChannelsEntity?
    suspend fun getById(id: Int): ChannelEntity?

    suspend fun setIsFavourite(id: Int, isFavourite: Boolean)

    fun getFlow(): Flow<List<ChannelEntity>>
    fun getFavouriteFlow(): Flow<List<ChannelEntity>>

    suspend fun setFilter(filter: String? = null)
    fun getFilter(): SharedFlow<String?>
}