package com.benkkstudios.ads.iron

import android.app.Activity
import com.benkkstudios.ads.iron.interfaces.InterstitialListener
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.base.SingletonHolderSingle
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSource.loadInterstitial


internal class OpenAdLoader() {
    companion object : SingletonHolderSingle<OpenAdLoader>(::OpenAdLoader)

    fun loadOpenAd(onComplete: (() -> Unit)? = null) {
        IronSource.setLevelPlayInterstitialListener(object : InterstitialListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                onComplete?.invoke()
            }

            override fun onAdFailedToLoad() {
                super.onAdFailedToLoad()
                onComplete?.invoke()
            }
        })
        loadInterstitial()
    }


    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (IronSource.isInterstitialReady()) {
            IronSource.setLevelPlayInterstitialListener(object : InterstitialListener() {
                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    onFailedToShow?.invoke()
                    openAdsShowing.set(false)
                    loadOpenAd()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    openAdsShowing.set(false)
                    loadOpenAd()
                }
            })
            IronSource.showInterstitial(activity)
        } else {
            onFailedToShow?.invoke()
        }
    }
}