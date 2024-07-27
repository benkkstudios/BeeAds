package com.benkkstudios.ads.max.natives

import android.content.Context
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.benkkstudios.ads.max.interfaces.NativeListener
import com.benkkstudios.ads.shared.base.SingletonHolder

internal class NativeLoader(private val adUnit: String) {
    companion object : SingletonHolder<NativeLoader, String>(::NativeLoader)

    fun load(context: Context, listener: NativeListener) {
        MaxNativeAdLoader(adUnit, context).apply {
            setNativeAdListener(object : MaxNativeAdListener() {
                override fun onNativeAdLoaded(p0: MaxNativeAdView?, ad: MaxAd) {
                    super.onNativeAdLoaded(p0, ad)
                    listener.onNativeAdLoaded(ad.nativeAd)
                }

                override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
                    super.onNativeAdLoadFailed(p0, p1)
                    listener.onNativeAdLoadFailed(p0)
                }
            })
            loadAd()
        }
    }
}