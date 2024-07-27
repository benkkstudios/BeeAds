package com.benkkstudios.ads.admob

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.benkkstudios.ads.admob.natives.NativeCompose
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.consent.ConsentManager
import com.benkkstudios.ads.shared.enums.NativeSize
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

abstract class AdmobFactoryImpl(unit: ProviderUnit) : BaseFactory(unit) {
    private val openAdLoader by lazy { OpenAdLoader.getInstance(unit) }
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        ConsentManager.getInstance().request(activity) {
            ioScope.launch {
                MobileAds.initialize(activity) {
                    mainScope.launch {
                        onInitializationComplete?.invoke()
                        loadInterstitial(activity)
                        loadReward(activity)
                    }
                }
            }
        }
    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        super.loadOpenAd(context, onComplete)
        openAdLoader.loadOpenAd(context, onComplete)
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
        NativeCompose(adUnit = unit.native, size, onAdFailedToLoad)
    }


    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        if (!ConsentManager.canRequestAds) return
        InterstitialAd.load(activity, unit.interstitial, AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            })
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (interstitialAd == null) {
            onError?.invoke("interstitialAd == null")
            loadInterstitial(activity)
            return
        }
        interstitialAd?.let {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onSuccess?.invoke()
                    loadInterstitial(activity)
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onError?.invoke(error.message)
                    loadInterstitial(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                }
            }
            interstitialAd?.show(activity)
        }
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        if (!ConsentManager.canRequestAds) return
        RewardedAd.load(activity, unit.reward, AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
            })
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (rewardedAd == null) {
            onError?.invoke("rewardedAd == null")
            loadReward(activity)
            return
        }
        val rewardCompleted = AtomicBoolean(false)
        rewardedAd?.let {
            rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    if (rewardCompleted.get()) onSuccess?.invoke()
                    loadReward(activity)
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    onError?.invoke(error.message)
                    loadReward(activity)
                }

                override fun onAdShowedFullScreenContent() {
                    rewardedAd = null
                }

            }
            rewardedAd?.show(activity) {
                rewardCompleted.set(true)
            }
        }

    }
}