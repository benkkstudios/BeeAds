package com.benkkstudios.ads.max

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkInitializationConfiguration
import com.benkkstudios.ads.max.interfaces.InterstitialListener
import com.benkkstudios.ads.max.interfaces.RewardListener
import com.benkkstudios.ads.max.natives.NativeCompose
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.enums.NativeSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MaxFactory(unit: ProviderUnit) : BaseFactory(unit) {
    private var interstitialAd: MaxInterstitialAd? = null
    private var rewardedAd: MaxRewardedAd? = null
    private val openAdLoader: OpenAdLoader by lazy { OpenAdLoader.getInstance(unit) }
    private val mainScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    override fun setCompany(): Provider = Provider.APPLOVIN
    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        val initConfig = AppLovinSdkInitializationConfiguration.builder(unit.key, activity)
            .setMediationProvider(AppLovinMediationProvider.MAX)
            .build()
        AppLovinSdk.getInstance(activity).initialize(initConfig) {
            mainScope.launch {
                onInitializationComplete?.invoke()
                loadInterstitial(activity)
                loadReward(activity)
            }
        }
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
        NativeCompose(unit.native, size, onAdFailedToLoad)
    }


    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        interstitialAd = MaxInterstitialAd(unit.interstitial, activity).apply {
            setListener(object : InterstitialListener() {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    interstitialAd = null
                }
            })
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
            if (isReady) {
                setListener(object : InterstitialListener() {
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
                })
                showAd(activity)
            }
        }
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        rewardedAd = MaxRewardedAd.getInstance(unit.reward, activity).apply {
            setListener(object : RewardListener() {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    rewardedAd = null
                }
            })
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
            if (isReady) {
                setListener(object : RewardListener() {
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
                showAd(activity)
            }
        }
    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        super.loadOpenAd(context, onComplete)
        openAdLoader.loadOpenAd(context, onComplete)
    }

    override fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        super.provider
        openAdLoader.showOpenAd(activity, onFailedToShow)
    }


}

