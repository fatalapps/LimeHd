package com.apps.fatal.data.models.base

import androidx.room.Entity
import com.apps.fatal.app_domain.repositories.entities.BaseEntity

interface BaseDataEntity<T : BaseEntity> {
    fun toEntity(): T
}

@Entity
abstract class FavouriteBaseDataEntity<T : BaseEntity> : BaseDataEntity<T> {
    var isFavourite: Boolean = false
}