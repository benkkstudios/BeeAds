package com.benkkstudios.ads.startapp.natives

import android.content.Context
import com.benkkstudios.ads.shared.base.SingletonHolderSingle
import com.benkkstudios.ads.startapp.NativeListener
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.adlisteners.AdEventListener


@Suppress("DEPRECATION")
internal class NativeLoader {
    companion object : SingletonHolderSingle<NativeLoader>(::NativeLoader) {
        fun load(context: Context, listener: NativeListener) {
            StartAppNativeAd(context).apply {
                val nativePrefs =
                    NativeAdPreferences().setAdsNumber(1).setAutoBitmapDownload(true).setPrimaryImageSize(2)
                NativeAdPreferences().setAdsNumber(1).setAutoBitmapDownload(true).setPrimaryImageSize(2)
                val adListener: AdEventListener = object : AdEventListener {
                    override fun onReceiveAd(ads: Ad) {
                        listener.onLoaded(nativeAds.random())
                    }

                    override fun onFailedToReceiveAd(error: Ad?) {
                        listener.onAdFailedToLoad(error?.errorMessage ?: "Unknown Error")
                    }
                }
                loadAd(nativePrefs, adListener)
            }
        }
    }


}