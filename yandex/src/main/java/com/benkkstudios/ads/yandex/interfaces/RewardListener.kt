package com.benkkstudios.ads.yandex.interfaces

import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import java.util.concurrent.atomic.AtomicBoolean

open class RewardListener : RewardedAdEventListener {
    private val rewardCompleted = AtomicBoolean(false)
    open fun onAdDismissedFullScreenContent() {}
    open fun onAdShowedFullScreenContent() {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    override fun onAdDismissed() {
        if (rewardCompleted.get()) {
            onAdDismissedFullScreenContent()
        }
    }

    override fun onAdFailedToShow(adError: AdError) {
        rewardCompleted.set(false)
        onAdFailedToShowFullScreenContent(adError.description)
    }

    override fun onAdShown() {
        onAdShowedFullScreenContent()
    }

    override fun onRewarded(reward: Reward) {
        rewardCompleted.set(true)
    }

    override fun onAdImpression(impressionData: ImpressionData?) {}
    override fun onAdClicked() {}
}