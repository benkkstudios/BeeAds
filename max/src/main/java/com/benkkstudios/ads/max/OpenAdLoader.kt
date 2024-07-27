@file:Suppress("DEPRECATION")

package com.benkkstudios.ads.max

import android.app.Activity
import android.content.Context
import com.applovin.mediation.ads.MaxAppOpenAd
import com.applovin.sdk.AppLovinSdk
import com.benkkstudios.ads.max.interfaces.OpenListener
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.SingletonHolder


internal class OpenAdLoader(private val unit: ProviderUnit) {
    companion object : SingletonHolder<OpenAdLoader, ProviderUnit>(::OpenAdLoader)

    private var appOpenAd: MaxAppOpenAd? = null

    fun loadOpenAd(context: Context, onComplete: (() -> Unit)? = null) {
        appOpenAd = MaxAppOpenAd(unit.open, context).apply {
            setListener(object : OpenListener() {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    onComplete?.invoke()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    onComplete?.invoke()
                }
            })
            loadAd()
        }
    }

    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (openAdsShowing.get()) return
        if (!AppLovinSdk.getInstance(activity).isInitialized) return
        if (appOpenAd == null) {
            loadOpenAd(activity)
            return
        }
        appOpenAd?.run {
            setListener(object : OpenListener() {
                override fun onAdFailedToShowFullScreenContent(error: String) {
                    super.onAdFailedToShowFullScreenContent(error)
                    appOpenAd = null
                    openAdsShowing.set(false)
                    loadOpenAd(activity)
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    onFailedToShow?.invoke()
                    appOpenAd = null
                    openAdsShowing.set(false)
                    loadOpenAd(activity)
                }
            })
            openAdsShowing.set(true)
            showAd(unit.open)
        }
    }
}