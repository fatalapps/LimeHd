package com.apps.fatal.limehdtest.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.apps.fatal.common_domain.string
import com.apps.fatal.common_ui.mvvm.BaseFragment
import com.apps.fatal.common_ui.mvvm.BaseViewModelFactory
import com.apps.fatal.limehdtest.R
import com.apps.fatal.limehdtest.common.getAppInjector
import com.apps.fatal.limehdtest.databinding.MainPagerLayoutBinding
import com.apps.fatal.limehdtest.ui.fragments.ChannelsListFragment
import com.apps.fatal.limehdtest.ui.viewmodels.MainPagerViewModel
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class MainPagerFragment : BaseFragment<MainPagerViewModel, MainPagerLayoutBinding>() {

    private val viewPager get() = binding.viewPager
    private val tabs get() = binding.tabs
    private val searchBar get() = binding.searchBar

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory

    companion object {

        private const val PAGES_COUNT = 2
    }

    private val tabLayoutMediator
        get() = TabLayoutMediator(tabs, viewPager, true) { tab, position ->
            when (position) {
                0 -> tab.text = string(R.string.all)
                1 -> tab.text = string(R.string.favourites)
            }
        }

    override fun onCreate(view: View): View {
        viewPager.adapter = PagerAdapter(this)
        tabLayoutMediator.attach()
        searchBar.addTextChangedListener(searchWatcher)
        return view
    }

    override fun getInflater(inflater: LayoutInflater, vg: ViewGroup?, attach: Boolean) = MainPagerLayoutBinding.inflate(inflater, vg, attach)

    private val searchWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {}

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            val text = s?.toString()?.trim()
            viewModel.setFilter(text)
        }
    }

    private inner class PagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = PAGES_COUNT

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> ChannelsListFragment.getInstance(isFavourite = false)
            1 -> ChannelsListFragment.getInstance(isFavourite = true)
            else -> throw Exception("invalid page number.")
        }

    }

    override fun provideViewModel(): MainPagerViewModel {
        getAppInjector().inject(this)
        return ViewModelProvider(this, viewModelFactory)[MainPagerViewModel::class.java]
    }
}