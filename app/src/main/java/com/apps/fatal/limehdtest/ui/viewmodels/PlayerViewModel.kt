package com.apps.fatal.limehdtest.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.apps.fatal.app_domain.repositories.PlaylistRepository
import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.common_ui.mvvm.BaseViewModel
import com.apps.fatal.common_ui.mvvm.ViewModelParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(private val playlistRepository: PlaylistRepository) : BaseViewModel() {

    private val mutableStateFlow = MutableStateFlow<ChannelEntity?>(null)
    val stateFlow get() = mutableStateFlow.asStateFlow()

    init {
        doOnParamsReady {
            if (it is Params) {
                loadChannel(it.id)
            }
        }
    }

    private fun loadChannel(_id: Int?) = viewModelScope.launch(Dispatchers.IO) {
        _id?.let { id ->
            val ch = playlistRepository.getById(id)
            ch?.let {
                mutableStateFlow.emit(it)
            }
        }
    }

    data class Params(val id: Int?) : ViewModelParams()

}