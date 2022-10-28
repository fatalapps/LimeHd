package com.apps.fatal.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.app_domain.repositories.entities.Current
import com.apps.fatal.data.models.base.BaseDataEntity
import com.apps.fatal.data.models.base.FavouriteBaseDataEntity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ChannelDataEntity")
internal data class ChannelDataEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("epg_id")
    val epgId: Int,
    @SerializedName("name_ru")
    val nameRu: String?,
    @SerializedName("name_en")
    val nameEn: String?,
    @SerializedName("vitrina_events_url")
    val vitrinaEventsUrl: String?,
    @SerializedName("is_federal")
    val isFederal: Boolean?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("cdn")
    val cdn: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("url_sound")
    val urlSound: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("hasEpg")
    val hasEpg: Boolean?,
    @SerializedName("current")
    val current: CurrentData?,
    @SerializedName("region_code")
    val regionCode: Int?,
    @SerializedName("tz")
    val tz: Int?,
    @SerializedName("is_foreign")
    val isForeign: Boolean?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("drm_status")
    val drmStatus: Int?,
    @SerializedName("owner")
    val owner: String?,
    @SerializedName("foreign_player_key")
    val foreignPlayerKey: Boolean?
) : FavouriteBaseDataEntity<ChannelEntity>() {

    override fun toEntity(): ChannelEntity {
        val isFav = isFavourite

        return ChannelEntity(
            id = id,
            epgId = epgId,
            nameRu = nameRu,
            nameEn = nameEn,
            vitrinaEventsUrl = vitrinaEventsUrl,
            isFederal = isFederal ?: false,
            address = address,
            cdn = cdn,
            url = url,
            urlSound = urlSound,
            image = image,
            hasEpg = hasEpg ?: false,
            current = current?.toEntity() ?: Current(),
            regionCode = regionCode,
            tz = tz,
            isForeign = isForeign ?: false,
            number = number,
            drmStatus = drmStatus,
            owner = owner,
            foreignPlayerKey = foreignPlayerKey ?: false
        ).apply {
            isFavourite = isFav
        }
    }
}

data class CurrentData(
    @SerializedName("timestart")
    val timeStart: Long? = 0,
    @SerializedName("timestop")
    val timeStop: Long? = 0,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("desc")
    val desc: String? = null,
    @SerializedName("cdnvideo")
    val cdnVideo: Int? = null,
    @SerializedName("rating")
    val rating: Int? = null
) : BaseDataEntity<Current> {

    override fun toEntity() = Current(
        timeStart = timeStart ?: 0,
        timeStop = timeStop ?: 0,
        title = title,
        desc = desc,
        cdnVideo = cdnVideo,
        rating = rating
    )
}