package com.benkkstudios.ads.yandex.natives

import android.content.Context
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.benkkstudios.ads.yandex.interfaces.NativeListener
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener
import com.yandex.mobile.ads.nativeads.NativeAdLoader
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration

internal class NativeLoder(private val adUnit: String) {
    companion object : SingletonHolder<NativeLoder, String>(::NativeLoder)

    fun load(context: Context, nativeListener: NativeListener) {
        NativeAdLoader(context).apply {
            setNativeAdLoadListener(object : NativeAdLoadListener {
                override fun onAdLoaded(nativeAd: NativeAd) {
                    nativeListener.onLoaded(nativeAd)
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    nativeListener.onAdFailedToLoad(error.description)
                }
            })
        }.also {
            it.loadAd(buildAdRequestConfiguration(adUnit))
        }
    }


    private fun buildAdRequestConfiguration(adUnit: String): NativeAdRequestConfiguration {
        return NativeAdRequestConfiguration
            .Builder(adUnit)
            .setShouldLoadImagesAutomatically(true)
            .build()
    }
}

