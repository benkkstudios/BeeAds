package com.benkkstudios.ads.meta.interfaces

import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAdListener

internal open class InterstitialListener : InterstitialAdListener {
    open fun onAdLoaded() {}
    open fun onAdFailedToLoad() {}
    open fun onAdShowingFullScreenContent() {}
    open fun onAdDismissedFullScreenContent() {}
    override fun onError(p0: Ad?, p1: AdError?) {
        onAdFailedToLoad()
    }

    override fun onAdLoaded(p0: Ad?) {
        onAdLoaded()
    }

    override fun onAdClicked(p0: Ad?) {
    }

    override fun onLoggingImpression(p0: Ad?) {
    }

    override fun onInterstitialDisplayed(p0: Ad?) {
        onAdShowingFullScreenContent()
    }

    override fun onInterstitialDismissed(p0: Ad?) {
        onAdDismissedFullScreenContent()
    }
}