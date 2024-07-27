package com.benkkstudios.ads.iron.interfaces

import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.model.Placement
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener
import java.util.concurrent.atomic.AtomicBoolean

internal open class RewardListener : LevelPlayRewardedVideoListener {
    private val rewardCompleted = AtomicBoolean(false)
    open fun onAdDismissedFullScreenContent(rewardCompleted: Boolean) {}
    open fun onAdFailedToShowFullScreenContent(error: String) {}

    override fun onAdOpened(p0: AdInfo?) {
    }

    override fun onAdShowFailed(p0: IronSourceError?, p1: AdInfo?) {
        onAdFailedToShowFullScreenContent(p0?.errorMessage ?: "Unknown Error")
    }

    override fun onAdClicked(p0: Placement?, p1: AdInfo?) {
    }

    override fun onAdRewarded(p0: Placement?, p1: AdInfo?) {
        rewardCompleted.set(true)
    }

    override fun onAdClosed(p0: AdInfo?) {
        onAdDismissedFullScreenContent(rewardCompleted.get())
    }

    override fun onAdAvailable(p0: AdInfo?) {
    }

    override fun onAdUnavailable() {
    }
}