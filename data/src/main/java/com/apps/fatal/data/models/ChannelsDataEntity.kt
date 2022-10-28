package com.apps.fatal.data.models

import com.apps.fatal.app_domain.repositories.entities.ChannelsEntity
import com.apps.fatal.data.models.base.BaseDataEntity
import com.google.gson.annotations.SerializedName

internal data class ChannelsDataEntity(
    @SerializedName("channels")
    val channels: List<ChannelDataEntity>? = null
): BaseDataEntity<ChannelsEntity> {

    override fun toEntity() = ChannelsEntity(
        channels = channels?.map { it.toEntity() } ?: emptyList()
    )
}