package com.benkkstudios.ads.core.manager

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.benkkstudios.ads.core.ActivityHolder
import com.benkkstudios.ads.shared.base.BaseFactory

internal class OpenAdsManager(
    private val _primaryManager: BaseFactory,
    private val _secondaryManager: BaseFactory
) : ActivityHolder(), DefaultLifecycleObserver {
    fun init(application: Application?) {
        application?.let {
            application.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
            _primaryManager.loadOpenAd(application) {
                _secondaryManager.loadOpenAd(application)
            }
        }
    }


    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        currentActivity()?.let {
            _primaryManager.showOpenAd(it) {
                _secondaryManager.showOpenAd(it)
            }
        }
    }
}