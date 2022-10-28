package com.apps.fatal.limehdtest.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.apps.fatal.app_domain.repositories.PlaylistRepository
import com.apps.fatal.common_ui.mvvm.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelsListViewModel @Inject constructor(private val playlistRepository: PlaylistRepository) : BaseViewModel() {

    private val favouritesFlow get() = playlistRepository.getFavouriteFlow()
    private val allFlow get() = playlistRepository.getFlow()

    var isFavourites: Boolean = false

    init {
        load()
    }

    fun load() = viewModelScope.launch {
        playlistRepository.getChannels()
    }

    fun getFlow() = if (isFavourites) favouritesFlow else allFlow

    fun filterFlow() = playlistRepository.getFilter()

    fun setIsFavourite(id: Int, state: Boolean) = viewModelScope.launch {
        playlistRepository.setIsFavourite(id, state)
    }

}