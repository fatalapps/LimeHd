package com.apps.fatal.app_domain.repositories.entities

import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelsEntity(
    val channels: List<ChannelEntity>
): BaseEntity