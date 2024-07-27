package com.benkkstudios.ads.meta

import android.content.Context
import com.benkkstudios.ads.meta.interfaces.InterstitialListener
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.facebook.ads.InterstitialAd

class OpenAdLoader(private val unit: ProviderUnit) {
    companion object : SingletonHolder<OpenAdLoader, ProviderUnit>(::OpenAdLoader)

    private var interstitialAd: InterstitialAd? = null
    fun loadOpenAd(context: Context, onComplete: (() -> Unit)? = null) {
        interstitialAd = InterstitialAd(context, unit.interstitial).apply {
            loadAd(
                buildLoadAdConfig()
                    .withAdListener(object : InterstitialListener() {
                        override fun onAdFailedToLoad() {
                            super.onAdFailedToLoad()
                            onComplete?.invoke()
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            onComplete?.invoke()
                        }

                        override fun onAdShowingFullScreenContent() {
                            super.onAdShowingFullScreenContent()
                            interstitialAd = null
                            openAdsShowing.set(true)
                        }
                    })
                    .build()
            )
        }
    }


    fun showOpenAd(onFailedToShow: (() -> Unit)?) {
        interstitialAd?.run {
            openAdsShowing.set(false)
            if (!isAdLoaded) {
                onFailedToShow?.invoke()
                return
            }
            if (isAdInvalidated) {
                onFailedToShow?.invoke()
                return
            }
            show()
        }
    }
}