package com.benkkstudios.ads.wortise.interfaces

import com.wortise.ads.AdError
import com.wortise.ads.interstitial.InterstitialAd

open class InterstitialListener : InterstitialAd.Listener {
    open fun onAdDismissedFullScreenContent() {}
    open fun onAdShowedFullScreenContent() {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    open fun onAdFailedToLoad() {}
    open fun onAdLoaded() {}
    override fun onInterstitialDismissed(ad: InterstitialAd) {
        super.onInterstitialDismissed(ad)
        onAdDismissedFullScreenContent()
    }

    override fun onInterstitialFailedToLoad(ad: InterstitialAd, error: AdError) {
        super.onInterstitialFailedToLoad(ad, error)
        onAdFailedToLoad()
    }

    override fun onInterstitialFailedToShow(ad: InterstitialAd, error: AdError) {
        super.onInterstitialFailedToShow(ad, error)
        onAdFailedToShowFullScreenContent(error.message)
    }

    override fun onInterstitialLoaded(ad: InterstitialAd) {
        super.onInterstitialLoaded(ad)
        onAdLoaded()
    }

    override fun onInterstitialShown(ad: InterstitialAd) {
        super.onInterstitialShown(ad)
        onAdShowedFullScreenContent()
    }
}