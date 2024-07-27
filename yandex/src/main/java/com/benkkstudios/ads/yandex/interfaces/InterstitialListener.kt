package com.benkkstudios.ads.yandex.interfaces

import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

open class InterstitialListener : InterstitialAdEventListener {
    open fun onAdDismissedFullScreenContent() {}
    open fun onAdShowedFullScreenContent() {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    override fun onAdDismissed() {
        onAdDismissedFullScreenContent()
    }

    override fun onAdFailedToShow(adError: AdError) {
        onAdFailedToShowFullScreenContent(adError.description)
    }

    override fun onAdShown() {
        onAdShowedFullScreenContent()
    }

    override fun onAdClicked() {}
    override fun onAdImpression(impressionData: ImpressionData?) {}
}