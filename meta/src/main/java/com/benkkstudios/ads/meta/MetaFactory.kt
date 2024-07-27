package com.benkkstudios.ads.meta

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.benkkstudios.ads.meta.interfaces.InterstitialListener
import com.benkkstudios.ads.meta.interfaces.RewardListener
import com.benkkstudios.ads.meta.natives.NativeCompose
import com.benkkstudios.ads.shared.AdsConstant
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.adLogging
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.enums.NativeSize
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.InterstitialAd
import com.facebook.ads.RewardedVideoAd

class MetaFactory(unit: ProviderUnit) : BaseFactory(unit) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedVideoAd? = null
    private var onInterstitialDismiss: (() -> Unit)? = null
    private var onRewardDismiss: (() -> Unit)? = null
    private val openAdLoader: OpenAdLoader by lazy { OpenAdLoader.getInstance(unit) }
    override fun setCompany() = Provider.META
    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        if (!AudienceNetworkAds.isInitialized(activity)) {
            AdSettings.setTestMode(AdsConstant.DEBUG_MODE)
            AudienceNetworkAds
                .buildInitSettings(activity)
                .withInitListener {
                    onInitializationComplete?.invoke()
                    loadInterstitial(activity)
                    loadReward(activity)
                }
                .initialize()
        }
    }

    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        interstitialAd = InterstitialAd(activity, unit.interstitial).apply {
            loadAd(
                buildLoadAdConfig()
                    .withAdListener(object : InterstitialListener() {
                        override fun onAdFailedToLoad() {
                            super.onAdFailedToLoad()
                            interstitialAd = null
                        }

                        override fun onAdShowingFullScreenContent() {
                            super.onAdShowingFullScreenContent()
                            interstitialAd = null
                        }

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            onInterstitialDismiss?.invoke()
                        }
                    })
                    .build()
            )
        }
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (interstitialAd == null) {
            onError?.invoke("interstitialAd == null")
            loadInterstitial(activity)
            return
        }
        onInterstitialDismiss = onSuccess
        interstitialAd?.run {
            if (!isAdLoaded) {
                onError?.invoke("Ad not loaded")
                return
            }
            if (isAdInvalidated) {
                onError?.invoke("Ad Invalidated")
                return
            }
            show()
        }
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        rewardedAd = RewardedVideoAd(activity, unit.reward).apply {
            loadAd(
                buildLoadAdConfig()
                    .withAdListener(object : RewardListener() {
                        override fun onAdFailedToLoad() {
                            super.onAdFailedToLoad()
                            rewardedAd = null
                        }

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            onRewardDismiss?.invoke()
                        }
                    })
                    .build()
            );
        }
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (rewardedAd == null) {
            onError?.invoke("rewardedAd == null")
            loadReward(activity)
            return
        }
        onRewardDismiss = onSuccess
        rewardedAd?.run {
            if (!isAdLoaded) {
                onError?.invoke("Ad not loaded")
                return
            }
            if (isAdInvalidated) {
                onError?.invoke("Ad Invalidated")
                return
            }
            show()
        }
    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        super.loadOpenAd(context, onComplete)
        openAdLoader.loadOpenAd(context, onComplete)
    }

    override fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        openAdLoader.showOpenAd(onFailedToShow)
    }

    @Composable
    override fun BannerCompose(size: BannerSize, onAdFailedToLoad: () -> Unit) {
        super.BannerCompose(size, onAdFailedToLoad)
        val adView = rememberBanner(size = size, adUnitId = unit.banner, onAdFailedToLoad)
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
        NativeCompose(adUnit = unit.native, size = size, onAdFailedToLoad = onAdFailedToLoad)
    }
}