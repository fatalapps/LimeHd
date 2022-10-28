package com.apps.fatal.data.api

import com.apps.fatal.data.models.ChannelsDataEntity
import retrofit2.http.GET

internal interface PlaylistApi {

    private companion object {

        const val ROUTE = "playlist"
        const val CHANNELS = "$ROUTE/channels.json"
    }

    @GET(CHANNELS)
    suspend fun getChannels(): ChannelsDataEntity?
}