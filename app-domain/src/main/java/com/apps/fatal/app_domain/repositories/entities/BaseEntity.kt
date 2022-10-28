package com.apps.fatal.app_domain.repositories.entities

import android.os.Parcelable

interface BaseEntity : Parcelable

abstract class FavouriteBaseEntity : BaseEntity {
    var isFavourite: Boolean = false
}