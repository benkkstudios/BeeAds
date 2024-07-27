package com.benkkstudios.ads.iron.interfaces

import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener

internal open class InterstitialListener : LevelPlayInterstitialListener {
    open fun onAdLoaded() {}
    open fun onAdFailedToLoad() {}
    open fun onAdDismissedFullScreenContent() {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}
    override fun onAdReady(p0: AdInfo?) {
        onAdLoaded()
    }

    override fun onAdLoadFailed(p0: IronSourceError?) {
        onAdFailedToLoad()
    }

    override fun onAdOpened(p0: AdInfo?) {
    }

    override fun onAdShowSucceeded(p0: AdInfo?) {
        onAdDismissedFullScreenContent()
    }

    override fun onAdShowFailed(p0: IronSourceError?, p1: AdInfo?) {
        onAdFailedToShowFullScreenContent(p0?.errorMessage ?: "Unknown Error")
    }

    override fun onAdClicked(p0: AdInfo?) {
    }

    override fun onAdClosed(p0: AdInfo?) {
    }
}