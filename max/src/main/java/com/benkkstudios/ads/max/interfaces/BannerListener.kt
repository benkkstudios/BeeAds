package com.benkkstudios.ads.max.interfaces

import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError

open class BannerListener : MaxAdViewAdListener {
    open fun onAdLoaded() {}
    open fun onAdFailedToLoad() {}
    override fun onAdLoaded(p0: MaxAd) {
        onAdLoaded()
    }

    override fun onAdDisplayed(p0: MaxAd) {
    }

    override fun onAdHidden(p0: MaxAd) {
    }

    override fun onAdClicked(p0: MaxAd) {
    }

    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        onAdFailedToLoad()
    }

    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
    }

    override fun onAdExpanded(p0: MaxAd) {
    }

    override fun onAdCollapsed(p0: MaxAd) {
    }
}