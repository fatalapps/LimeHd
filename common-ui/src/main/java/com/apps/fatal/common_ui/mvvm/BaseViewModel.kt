package com.apps.fatal.common_ui.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {

    var params: ViewModelParams? = null
    private var savedInstanceState: Bundle? = null

    private var paramsReadyListeners = arrayListOf<(params: ViewModelParams?) -> Unit>()

    fun doOnParamsReady(unit: (params: ViewModelParams?) -> Unit) {
        paramsReadyListeners.add(unit)
    }

    open fun onSaveInstanceState(bundle: Bundle?) = Unit

    @CallSuper
    open fun onViewStateRestored(bundle: Bundle?) {
        if (bundle == null || savedInstanceState == null) {
            savedInstanceState = bundle
            for (p in paramsReadyListeners) p.invoke(params)
            paramsReadyListeners.clear()
        }
        savedInstanceState = bundle
    }
}

open class ViewModelParams