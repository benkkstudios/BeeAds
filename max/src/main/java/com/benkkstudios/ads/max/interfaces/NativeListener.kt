package com.benkkstudios.ads.max.interfaces

import com.applovin.mediation.nativeAds.MaxNativeAd

interface NativeListener {
    fun onNativeAdLoaded(ad: MaxNativeAd?)
    fun onNativeAdLoadFailed(error: String)
}