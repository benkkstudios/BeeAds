package com.benkkstudios.ads.unity

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
import com.benkkstudios.ads.unity.natives.rememberNative
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions
import java.util.concurrent.atomic.AtomicBoolean

class UnityFactory(unit: ProviderUnit) : BaseFactory(unit) {
    private val isInterstitialLoaded = AtomicBoolean(false)
    private val isRewardLoaded = AtomicBoolean(false)
    private val openAdLoader by lazy { OpenAdLoader.getInstance(unit) }
    override fun setCompany(): Provider = Provider.UNITY
    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        UnityAds.initialize(activity, unit.key, false, object : IUnityAdsInitializationListener {
            override fun onInitializationComplete() {
                onInitializationComplete?.invoke()
                loadInterstitial(activity)
                loadReward(activity)
            }

            override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError?, message: String?) {}
        })
    }

    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        if (isInterstitialLoaded.get()) {
            return
        }
        UnityAds.load(unit.interstitial, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String?) {
                isInterstitialLoaded.set(true)
            }

            override fun onUnityAdsFailedToLoad(placementId: String?, error: UnityAds.UnityAdsLoadError?, message: String?) {
                isInterstitialLoaded.set(false)
            }
        })
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (isInterstitialLoaded.get().not()) {
            onError?.invoke("Interstitial not loaded")
            loadInterstitial(activity)
            return
        }
        UnityAds.show(activity, unit.interstitial, UnityAdsShowOptions(), object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(placementId: String?, error: UnityAds.UnityAdsShowError?, message: String?) {
                onError?.invoke(message ?: "Unknown error")
                loadInterstitial(activity)
            }

            override fun onUnityAdsShowStart(placementId: String?) {
            }

            override fun onUnityAdsShowClick(placementId: String?) {
            }

            override fun onUnityAdsShowComplete(placementId: String?, state: UnityAds.UnityAdsShowCompletionState?) {
                onSuccess?.invoke()
                loadInterstitial(activity)
            }
        })
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        UnityAds.load(unit.reward, object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(placementId: String?) {
                isRewardLoaded.set(true)
            }

            override fun onUnityAdsFailedToLoad(placementId: String?, error: UnityAds.UnityAdsLoadError?, message: String?) {
                isRewardLoaded.set(false)
            }
        })
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (isInterstitialLoaded.get().not()) {
            onError?.invoke("Reward not loaded")
            loadReward(activity)
            return
        }
        UnityAds.show(activity, unit.reward, UnityAdsShowOptions(), object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(placementId: String?, error: UnityAds.UnityAdsShowError?, message: String?) {
                onError?.invoke(message ?: "Unknown error")
                loadReward(activity)
            }

            override fun onUnityAdsShowStart(placementId: String?) {
            }

            override fun onUnityAdsShowClick(placementId: String?) {
            }

            override fun onUnityAdsShowComplete(placementId: String?, state: UnityAds.UnityAdsShowCompletionState?) {
                onSuccess?.invoke()
                loadReward(activity)
            }
        })
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
        val adView = rememberNative(adUnitId = unit.native, size = size, onAdFailedToLoad)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { adView },
        )
    }
}