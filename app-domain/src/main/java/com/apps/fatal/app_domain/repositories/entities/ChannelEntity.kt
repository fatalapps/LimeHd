package com.apps.fatal.app_domain.repositories.entities

import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelEntity(
    val id: Int,
    val epgId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val vitrinaEventsUrl: String?,
    val isFederal: Boolean,
    val address: String?,
    val cdn: String?,
    val url: String?,
    val urlSound: String?,
    val image: String?,
    val hasEpg: Boolean,
    val current: Current,
    val regionCode: Int?,
    val tz: Int?,
    val isForeign: Boolean,
    val number: Int?,
    val drmStatus: Int?,
    val owner: String?,
    val foreignPlayerKey: Boolean
): FavouriteBaseEntity()

@Parcelize
data class Current(
    val timeStart: Long = 0,
    val timeStop: Long = 0,
    val title: String? = null,
    val desc: String? = null,
    val cdnVideo: Int? = null,
    val rating: Int? = null
): BaseEntity