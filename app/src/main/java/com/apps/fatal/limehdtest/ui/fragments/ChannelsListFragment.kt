package com.apps.fatal.limehdtest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.common_domain.color
import com.apps.fatal.common_domain.dp
import com.apps.fatal.common_ui.mvvm.BaseFragment
import com.apps.fatal.common_ui.recycler.BaseRecyclerAdapter
import com.apps.fatal.common_ui.mvvm.BaseViewModelFactory
import com.apps.fatal.limehdtest.common.getAppInjector
import com.apps.fatal.limehdtest.databinding.ChannelsListFragmentBinding
import com.apps.fatal.limehdtest.ui.holders.ChannelListItem
import com.apps.fatal.limehdtest.ui.viewmodels.ChannelsListViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChannelsListFragment : BaseFragment<ChannelsListViewModel, ChannelsListFragmentBinding>() {

    private val recyclerView get() = binding.recyclerView
    private val channelsAdapter = BaseRecyclerAdapter()

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory

    private var collectorJob: Job? = null

    companion object {
        private const val FAVOURITE_ARG = "isFavArg_"

        fun getInstance(isFavourite: Boolean) = ChannelsListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(FAVOURITE_ARG, isFavourite)
            }
        }

    }

    override fun getInflater(inflater: LayoutInflater, vg: ViewGroup?, attach: Boolean) = ChannelsListFragmentBinding.inflate(inflater, vg, attach)

    override fun provideViewModel(): ChannelsListViewModel {
        getAppInjector().inject(this)
        return ViewModelProvider(this, viewModelFactory)[ChannelsListViewModel::class.java]
    }

    override fun onCreate(view: View): View {
        recyclerView.run {
            val decoration = MaterialDividerItemDecoration(requireContext(), (layoutManager as LinearLayoutManager).orientation).apply {
                dividerColor = color(com.apps.fatal.common_domain.R.color.windowBackground)
                dividerThickness = dp(8f).toInt()
            }
            addItemDecoration(decoration)
            adapter = channelsAdapter
        }

        viewModel.isFavourites = arguments?.getBoolean(FAVOURITE_ARG) ?: false

        return super.onCreate(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.filterFlow().collect {
                collectorJob?.cancel()
                collectorJob = lifecycleScope.launch { viewModel.getFlow().collectLatest(::setupChannels) }
            }
        }
    }

    private val channelCallback = object : ChannelListItem.Callback {

        override fun onClick(id: Int) {
            loadFragment(PlayerFragment.newInstance(id))
        }

        override fun onFavourite(id: Int, state: Boolean) {
            viewModel.setIsFavourite(id, state)
        }
    }

    private fun setupChannels(list: List<ChannelEntity>) {
        channelsAdapter.submitList(list.map { ch ->
            ChannelListItem(ch, channelCallback)
        })
    }
}