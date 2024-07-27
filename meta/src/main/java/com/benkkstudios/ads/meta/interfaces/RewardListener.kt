package com.benkkstudios.ads.meta.interfaces

import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.RewardedVideoAdListener
import java.util.concurrent.atomic.AtomicBoolean

internal open class RewardListener : RewardedVideoAdListener {
    private val rewardCompleted = AtomicBoolean(false)
    open fun onAdLoaded() {}
    open fun onAdFailedToLoad() {}
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

    override fun onRewardedVideoCompleted() {
        rewardCompleted.set(true)
    }

    override fun onRewardedVideoClosed() {
        if (rewardCompleted.get()) {
            onAdDismissedFullScreenContent()
        }
    }
}