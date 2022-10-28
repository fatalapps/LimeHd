package com.apps.fatal.limehdtest.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.apps.fatal.app_domain.repositories.PlaylistRepository
import com.apps.fatal.common_ui.mvvm.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainPagerViewModel @Inject constructor(private val playlistRepository: PlaylistRepository) : BaseViewModel() {

    fun setFilter(filter: String?) = viewModelScope.launch(Dispatchers.IO) {
        playlistRepository.setFilter(filter)
    }
}