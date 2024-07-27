package com.benkkstudios.ads.admob

import android.app.Activity
import android.content.Context
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback

internal class OpenAdLoader(private val unit: ProviderUnit) {
    companion object : SingletonHolder<OpenAdLoader, ProviderUnit>(::OpenAdLoader)

    private var appOpenAd: AppOpenAd? = null

    fun loadOpenAd(context: Context, onComplete: (() -> Unit)? = null) {
        AppOpenAd.load(
            context, unit.open, AdRequest.Builder().build(),
            object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    onComplete?.invoke()
                    appOpenAd = ad
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    onComplete?.invoke()
                    appOpenAd = null
                }
            },
        )
    }


    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (openAdsShowing.get()) {
            return
        }

        if (appOpenAd == null) {
            loadOpenAd(activity)
            return
        }

        appOpenAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    openAdsShowing.set(false)
                    loadOpenAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    onFailedToShow?.invoke()
                    appOpenAd = null
                    openAdsShowing.set(false)
                    loadOpenAd(activity)
                }
            }
        openAdsShowing.set(true)
        appOpenAd?.show(activity)
    }


}