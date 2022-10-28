package com.apps.fatal.common_ui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.apps.fatal.common_ui.R


abstract class BaseFragment<T : BaseViewModel, VB : ViewBinding> : Fragment(), BaseInflater<VB> {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    lateinit var viewModel: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getInflater(inflater, container, false)
        viewModel = provideViewModel()
        return onCreate(binding.root)
    }

    abstract fun provideViewModel(): T

    protected fun loadFragment(fragment: Fragment) {
        val backStateName: String = fragment.javaClass.name

        val manager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) { //fragment not in back stack, create it.
            val ft = manager.beginTransaction()
            ft.replace(R.id.container, fragment, backStateName)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    open fun createView(
        view: View,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = Unit

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.onViewStateRestored(savedInstanceState)
    }

    open fun onCreate(view: View): View {
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun interface BaseInflater<VB : ViewBinding> {
    fun getInflater(inflater: LayoutInflater, vg: ViewGroup?, attach: Boolean): VB
}