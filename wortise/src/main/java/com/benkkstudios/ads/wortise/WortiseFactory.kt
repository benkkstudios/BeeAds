package com.benkkstudios.ads.wortise

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.benkkstudios.ads.shared.AdsConstant
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.consent.ConsentManager
import com.benkkstudios.ads.shared.enums.NativeSize
import com.benkkstudios.ads.wortise.interfaces.InterstitialListener
import com.benkkstudios.ads.wortise.interfaces.RewardListener
import com.benkkstudios.ads.wortise.natives.NativeCompose
import com.wortise.ads.AdSettings
import com.wortise.ads.WortiseSdk
import com.wortise.ads.interstitial.InterstitialAd
import com.wortise.ads.rewarded.RewardedAd

class WortiseFactory(unit: ProviderUnit) : BaseFactory(unit) {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    override fun setCompany() = Provider.WORTISE

    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        AdSettings.testEnabled = AdsConstant.DEBUG_MODE
        ConsentManager.getInstance().request(activity) {
            WortiseSdk.initialize(activity, unit.key) {
                onInitializationComplete?.invoke()
                loadInterstitial(activity)
                loadReward(activity)
            }
        }

    }

    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        interstitialAd = InterstitialAd(activity, unit.interstitial).apply {
            listener = object : InterstitialListener() {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    interstitialAd = null
                }
            }
            loadAd()
        }
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (interstitialAd == null) {
            onError?.invoke("interstitialAd == null")
            loadInterstitial(activity)
            return
        }

        interstitialAd?.run {
            if (isAvailable) {
                listener = object : InterstitialListener() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        onSuccess?.invoke()
                        loadInterstitial(activity)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        interstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(error: String) {
                        super.onAdFailedToShowFullScreenContent(error)
                        onError?.invoke(error)
                        loadInterstitial(activity)
                    }
                }
                showAd(activity)
            } else {
                onError?.invoke("Unknown Error")
                loadInterstitial(activity)
            }
        }
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        rewardedAd = RewardedAd(activity, unit.reward).apply {
            listener = object : RewardListener() {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    rewardedAd = null
                }
            }
            loadAd()
        }
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (rewardedAd == null) {
            onError?.invoke("rewardedAd == null")
            loadReward(activity)
            return
        }

        rewardedAd?.run {
            if (isAvailable) {
                listener = object : RewardListener() {
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
                }
                showAd(activity)
            } else {
                onError?.invoke("Unknown Error")
                loadReward(activity)
            }
        }
    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        super.loadOpenAd(context, onComplete)
        OpenAdLoader.getInstance(unit).loadOpenAd(context)
    }

    override fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        OpenAdLoader.getInstance(unit).showOpenAd(activity, onFailedToShow)
    }

    @Composable
    override fun BannerCompose(size: BannerSize, onAdFailedToLoad: () -> Unit) {
        super.BannerCompose(size, onAdFailedToLoad)
        val adView = rememberBanner(size = size, adsUnitId = unit.banner, onAdFailedToLoad)
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
        NativeCompose(adUnit = unit.native, size = size, onAdFailedToLoad)
    }
}