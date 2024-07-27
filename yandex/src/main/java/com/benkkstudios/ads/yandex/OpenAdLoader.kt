package com.benkkstudios.ads.yandex

import android.app.Activity

import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.SingletonTriple
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

class OpenAdLoader(private val unit: ProviderUnit, private val activity: Activity) {
    companion object : SingletonTriple<OpenAdLoader, ProviderUnit, Activity>(::OpenAdLoader)

    private var appOpenAd: AppOpenAd? = null
    private val loader: AppOpenAdLoader by lazy { AppOpenAdLoader(activity) }
    fun loadOpenAd(onComplete: (() -> Unit)? = null) {
        with(loader) {
            setAdLoadListener(object : AppOpenAdLoadListener {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    this@OpenAdLoader.appOpenAd = appOpenAd
                    onComplete?.invoke()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    onComplete?.invoke()
                }
            })
            loadAd(AdRequestConfiguration.Builder(unit.open).build())
        }
    }


    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (openAdsShowing.get()) {
            return
        }

        if (appOpenAd == null) {
            loadOpenAd()
            return
        }
        appOpenAd?.run {
            setAdEventListener(object : AppOpenAdEventListener {
                override fun onAdShown() {
                    openAdsShowing.set(true)
                }

                override fun onAdFailedToShow(adError: AdError) {
                    onFailedToShow?.invoke()
                    appOpenAd = null
                    openAdsShowing.set(false)
                    loadOpenAd()
                }

                override fun onAdDismissed() {
                    appOpenAd = null
                    openAdsShowing.set(false)
                    loadOpenAd()
                }

                override fun onAdClicked() {
                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                }
            })
            show(activity)
        }
    }
}