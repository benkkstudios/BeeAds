package com.benkkstudios.ads.unity

import android.app.Activity
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions
import java.util.concurrent.atomic.AtomicBoolean

class OpenAdLoader(private val unit: ProviderUnit) {
    companion object : SingletonHolder<OpenAdLoader, ProviderUnit>(::OpenAdLoader)

    private val isOpenAdLoaded = AtomicBoolean(false)

    fun loadOpenAd(onComplete: (() -> Unit)? = null) {
        UnityAds.load(unit.interstitial, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String?) {
                onComplete?.invoke()
                isOpenAdLoaded.set(true)
            }

            override fun onUnityAdsFailedToLoad(placementId: String?, error: UnityAds.UnityAdsLoadError?, message: String?) {
                onComplete?.invoke()
                isOpenAdLoaded.set(false)
            }
        })
    }


    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (openAdsShowing.get()) {
            return
        }

        if (!isOpenAdLoaded.get()) {
            loadOpenAd()
            return
        }
        if (isOpenAdLoaded.get().not()) {
            onFailedToShow?.invoke()
            return
        }
        UnityAds.show(activity, unit.interstitial, UnityAdsShowOptions(), object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(placementId: String?, error: UnityAds.UnityAdsShowError?, message: String?) {
                onFailedToShow?.invoke()
                openAdsShowing.set(false)
                loadOpenAd()
            }

            override fun onUnityAdsShowStart(placementId: String?) {
                openAdsShowing.set(true)
            }

            override fun onUnityAdsShowClick(placementId: String?) {
            }

            override fun onUnityAdsShowComplete(placementId: String?, state: UnityAds.UnityAdsShowCompletionState?) {
                openAdsShowing.set(false)
                loadOpenAd()
            }
        })
    }
}