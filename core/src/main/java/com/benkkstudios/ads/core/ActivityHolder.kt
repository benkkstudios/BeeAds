package com.benkkstudios.ads.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing

internal open class ActivityHolder : Application.ActivityLifecycleCallbacks {
    private var currentActivity: Activity? = null
    fun currentActivity(): Activity? = currentActivity
    override fun onActivityStarted(activity: Activity) {
        if (openAdsShowing.get().not()) {
            currentActivity = activity
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
    override fun onActivityResumed(p0: Activity) {}
    override fun onActivityPaused(p0: Activity) {}
    override fun onActivityStopped(p0: Activity) {}
    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
}