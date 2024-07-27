package com.benkkstudios.ads.yandex

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.enums.NativeSize
import com.benkkstudios.ads.yandex.interfaces.InterstitialListener
import com.benkkstudios.ads.yandex.interfaces.RewardListener
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.MobileAds
import com.yandex.mobile.ads.common.MobileAds.enableDebugErrorIndicator
import com.yandex.mobile.ads.common.MobileAds.enableLogging
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class YandexFactory(unit: ProviderUnit) : BaseFactory(unit) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private lateinit var openAdLoader: OpenAdLoader
    override fun setCompany(): Provider = Provider.YANDEX
    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        openAdLoader = OpenAdLoader(unit, activity)
        enableDebugErrorIndicator(false)
        enableLogging(false)
        MobileAds.apply {
            initialize(activity) {
                onInitializationComplete?.invoke()
                loadInterstitial(activity)
                loadReward(activity)
            }
        }
    }

    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        InterstitialAdLoader(activity).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@YandexFactory.interstitialAd = interstitialAd

                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    interstitialAd = null
                }
            })
        }.also {
            val adRequestConfiguration = AdRequestConfiguration.Builder(unit.interstitial).build()
            it.loadAd(adRequestConfiguration)
        }

    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (interstitialAd == null) {
            onError?.invoke("Unknown Error")
            loadInterstitial(activity)
            return
        }
        interstitialAd?.run {
            setAdEventListener(object : InterstitialListener() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    onSuccess?.invoke()
                    loadInterstitial(activity)
                }

                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    onError?.invoke(error)
                    loadInterstitial(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    interstitialAd = null
                }
            })
            show(activity)
        }
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        RewardedAdLoader(activity).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewarded: RewardedAd) {
                    rewardedAd = rewarded
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    rewardedAd = null
                }
            })
        }.also {
            val adRequestConfiguration = AdRequestConfiguration.Builder(unit.reward).build()
            it.loadAd(adRequestConfiguration)
        }
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (rewardedAd == null) {
            onError?.invoke("Unknown Error")
            loadReward(activity)
            return
        }
        rewardedAd?.run {
            setAdEventListener(object : RewardListener() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    onSuccess?.invoke()
                    loadReward(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    rewardedAd = null
                }

                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    onError?.invoke(error)
                    loadReward(activity)
                }
            })
            show(activity)
        }

    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        super.loadOpenAd(context, onComplete)
        openAdLoader.loadOpenAd(onComplete)
    }

    override fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        openAdLoader.showOpenAd(activity, onFailedToShow)
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
        com.benkkstudios.ads.yandex.natives.NativeCompose(adUnit = unit.native, size = size, onAdFailedToLoad)
    }
}