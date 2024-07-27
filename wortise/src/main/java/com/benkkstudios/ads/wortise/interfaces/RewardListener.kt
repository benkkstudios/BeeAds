package com.benkkstudios.ads.wortise.interfaces

import com.wortise.ads.AdError
import com.wortise.ads.rewarded.RewardedAd
import com.wortise.ads.rewarded.models.Reward
import java.util.concurrent.atomic.AtomicBoolean

open class RewardListener : RewardedAd.Listener {
    private val rewardCompleted = AtomicBoolean(false)
    open fun onAdDismissedFullScreenContent() {}
    open fun onAdShowedFullScreenContent() {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    open fun onAdFailedToLoad() {}
    open fun onAdLoaded() {}

    override fun onRewardedCompleted(ad: RewardedAd, reward: Reward) {
        rewardCompleted.set(true)
    }

    override fun onRewardedDismissed(ad: RewardedAd) {
        if (rewardCompleted.get()) {
            rewardCompleted.set(false)
            onAdDismissedFullScreenContent()
        }
    }

    override fun onRewardedFailedToShow(ad: RewardedAd, error: AdError) {
        onAdFailedToShowFullScreenContent(error.message)
    }

    override fun onRewardedLoaded(ad: RewardedAd) {
        onAdLoaded()
    }

    override fun onRewardedFailedToLoad(ad: RewardedAd, error: AdError) {
        onAdFailedToLoad()
    }

    override fun onRewardedShown(ad: RewardedAd) {
        onAdShowedFullScreenContent()
    }
}