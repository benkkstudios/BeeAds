package com.benkkstudios.ads.max.interfaces


import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import java.util.concurrent.atomic.AtomicBoolean

open class RewardListener : MaxRewardedAdListener {
    private val rewardCompleted = AtomicBoolean(false)
    open fun onAdLoaded() {}
    open fun onAdFailedToLoad() {}
    open fun onAdDismissedFullScreenContent(rewardCompleted: Boolean) {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdLoaded(p0: MaxAd) {
        onAdLoaded()
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdDisplayed(p0: MaxAd) {
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdHidden(p0: MaxAd) {
        onAdDismissedFullScreenContent(rewardCompleted.get())
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdClicked(p0: MaxAd) {
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdLoadFailed(p0: String, p1: MaxError) {
        onAdFailedToLoad()
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
        onAdFailedToShowFullScreenContent(p1.message)
    }

    @Deprecated(message = "Deprecated", replaceWith = ReplaceWith("MaxInterstitialListener.onAdLoaded()"))
    override fun onUserRewarded(p0: MaxAd, p1: MaxReward) {
        rewardCompleted.set(true)
    }
}