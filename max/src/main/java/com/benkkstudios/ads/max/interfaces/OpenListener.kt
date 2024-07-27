package com.benkkstudios.ads.max.interfaces

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError

open class OpenListener : MaxAdListener {
    open fun onAdLoaded() {}
    open fun onAdFailedToLoad() {}
    open fun onAdDismissedFullScreenContent() {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdLoaded(p0: MaxAd) {
        onAdLoaded()
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdDisplayed()"))
    override fun onAdDisplayed(p0: MaxAd) {
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdHidden()"))
    override fun onAdHidden(p0: MaxAd) {
        onAdDismissedFullScreenContent()
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdClicked()"))
    override fun onAdClicked(p0: MaxAd) {
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoadFailed()"))
    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        onAdFailedToLoad()
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdDisplayFailed()"))
    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        onAdFailedToShowFullScreenContent(p1.message)
    }
}