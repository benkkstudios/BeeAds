package com.benkkstudios.ads.startapp

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
import com.benkkstudios.ads.startapp.natives.OpenAdLoader
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class StartAppFactory(unit: ProviderUnit) : BaseFactory(unit) {
    private val openAdLoader by lazy { OpenAdLoader.getInstance() }
    override fun setCompany() = Provider.STARTAPP
    private var interstitalAd: StartAppAd? = null
    private var rewardAd: StartAppAd? = null
    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        super.init(activity, onInitializationComplete)
        StartAppSDK.init(activity, unit.key)
        loadInterstitial(activity)
        loadReward(activity)
    }

    override fun loadInterstitial(activity: Activity) {
        super.loadInterstitial(activity)
        interstitalAd = StartAppAd(activity)
        interstitalAd?.loadAd(StartAppAd.AdMode.FULLPAGE, object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
                interstitalAd = null
            }
        })
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (interstitalAd == null) {
            onError?.invoke("interstital not loaded")
            loadInterstitial(activity)
            return
        }
        interstitalAd?.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {
                onSuccess?.invoke()
                loadInterstitial(activity)
            }

            override fun adDisplayed(ad: Ad) {
            }

            override fun adClicked(ad: Ad) {
            }

            override fun adNotDisplayed(ad: Ad) {
                onError?.invoke(ad.errorMessage ?: "Unknow Error")
                loadInterstitial(activity)
            }
        })
    }

    override fun loadReward(activity: Activity) {
        super.loadReward(activity)
        rewardAd = StartAppAd(activity)
        rewardAd?.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
                rewardAd = null
            }
        })
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        if (rewardAd == null) {
            onError?.invoke("rewardAd not loaded")
            loadReward(activity)
            return
        }
        rewardAd?.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {
                onSuccess?.invoke()
                loadReward(activity)
            }

            override fun adDisplayed(ad: Ad) {
            }

            override fun adClicked(ad: Ad) {
            }

            override fun adNotDisplayed(ad: Ad) {
                onError?.invoke(ad.errorMessage ?: "Unknow Error")
                loadReward(activity)
            }
        })
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
        val adview = rememberBanner(size, onAdFailedToLoad)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            factory = { adview },
        )
    }

    @Composable
    override fun NativeCompose(size: NativeSize, onAdFailedToLoad: () -> Unit) {
        super.NativeCompose(size, onAdFailedToLoad)
        com.benkkstudios.ads.startapp.natives.NativeCompose(onAdFailedToLoad)
    }
}