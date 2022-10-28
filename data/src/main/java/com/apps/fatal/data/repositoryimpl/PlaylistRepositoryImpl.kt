package com.apps.fatal.data.repositoryimpl

import com.apps.fatal.app_domain.repositories.PlaylistRepository
import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.app_domain.repositories.entities.ChannelsEntity
import com.apps.fatal.data.api.PlaylistApi
import com.apps.fatal.data.db.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PlaylistRepositoryImpl @Inject constructor(
    private val playlistApi: PlaylistApi,
    private val db: DatabaseRepository,
) : PlaylistRepository {

    private val dao by lazy { db.getPlaylistDao() }

    private val filterMutable = MutableStateFlow<String?>(null)

    override suspend fun getChannels(): ChannelsEntity? = withContext(Dispatchers.IO) {
        val result = playlistApi.getChannels()

        result?.channels?.let { list ->
            dao.upsertList(list)
        }

        return@withContext result?.toEntity()
    }

    override suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext dao.getById(id)?.toEntity()
    }

    override suspend fun setIsFavourite(id: Int, isFavourite: Boolean) = withContext(Dispatchers.IO) {
        dao.setIsFavourite(id, isFavourite)
    }

    override fun getFlow(): Flow<List<ChannelEntity>> = dao.getFlow().transform {
        emit(it.map { e -> e.toEntity() }.run {
            filterMutable.value?.run {
                if (isNotBlank()) getFiltered(this)
                else null
            } ?: this
        })
    }

    override fun getFavouriteFlow(): Flow<List<ChannelEntity>> =
        dao.getFavouriteFlow().transform {
            emit(it.map { e -> e.toEntity() }.run {
                filterMutable.value?.run {
                    if (isNotBlank()) getFiltered(this)
                    else null
                } ?: this
            })
        }

    override suspend fun setFilter(filter: String?) {
        filterMutable.emit(filter)
    }

    override fun getFilter(): SharedFlow<String?> = filterMutable.asSharedFlow()

    private fun List<ChannelEntity>.getFiltered(filter: String) =
        filter { it.nameRu?.lowercase()?.contains(filter.lowercase()) == true || it.nameEn?.lowercase()?.contains(filter.lowercase()) == true }
}