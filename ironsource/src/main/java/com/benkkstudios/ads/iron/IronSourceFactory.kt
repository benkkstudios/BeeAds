package com.benkkstudios.ads.iron


import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.benkkstudios.ads.iron.interfaces.InterstitialListener
import com.benkkstudios.ads.iron.interfaces.RewardListener
import com.benkkstudios.ads.iron.natives.rememberNative
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.benkkstudios.ads.shared.enums.NativeSize
import com.ironsource.mediationsdk.IronSource

class IronSourceFactory(unit: ProviderUnit) : BaseFactory(unit) {
    companion object : SingletonHolder<IronSourceFactory, ProviderUnit>(::IronSourceFactory)

    override fun setCompany(): Provider = Provider.IRONSOURCE

    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        IronSource.init(activity, unit.key) {
            onInitializationComplete?.invoke()
            loadInterstitial(activity)
            loadReward(activity)
        }
    }


    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        IronSource.loadInterstitial()
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (IronSource.isInterstitialReady()) {
            IronSource.setLevelPlayInterstitialListener(object : InterstitialListener() {
                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    onError?.invoke(error)
                    loadInterstitial(activity)
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    onSuccess?.invoke()
                    loadInterstitial(activity)
                }
            })
            IronSource.showInterstitial(activity)
        }
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        IronSource.loadRewardedVideo()
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (IronSource.isRewardedVideoAvailable()) {
            IronSource.setLevelPlayRewardedVideoListener(object : RewardListener() {
                override fun onAdDismissedFullScreenContent(rewardCompleted: Boolean) {
                    super.onAdDismissedFullScreenContent(rewardCompleted)
                    if (rewardCompleted) onSuccess?.invoke()
                    loadReward(activity)
                }

                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    onError?.invoke(error)
                    loadReward(activity)
                }
            })
            IronSource.showRewardedVideo(activity)
        }
    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        super.loadOpenAd(context, onComplete)
        OpenAdLoader.getInstance().loadOpenAd(onComplete)
    }

    override fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        OpenAdLoader.getInstance().showOpenAd(activity, onFailedToShow)
    }

    @Composable
    override fun BannerCompose(size: BannerSize, onAdFailedToLoad: () -> Unit) {
        super.BannerCompose(size, onAdFailedToLoad)
        val adView = rememberBanner(size, onAdFailedToLoad)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { adView },
        )
    }

    @Composable
    override fun NativeCompose(size: NativeSize, onAdFailedToLoad: () -> Unit) {
        super.NativeCompose(size, onAdFailedToLoad)
        val adView = rememberNative(size = size, onAdFailedToLoad)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { adView },
        )
    }
}