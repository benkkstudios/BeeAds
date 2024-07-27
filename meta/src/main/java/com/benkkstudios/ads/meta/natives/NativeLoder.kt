package com.benkkstudios.ads.meta.natives

import android.content.Context
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeAdBase
import com.facebook.ads.NativeAdListener

internal class NativeLoder(private val adUnit: String) {

    companion object : SingletonHolder<NativeLoder, String>(::NativeLoder)

    fun load(context: Context, nativeListener: NativeListener) {
        NativeAd(context, adUnit).apply {
            loadAd(
                buildLoadAdConfig()
                    .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
                    .withAdListener(object : NativeAdListener {
                        override fun onError(p0: Ad?, error: AdError?) {
                            nativeListener.onAdFailedToLoad(error?.errorMessage ?: "Unknown Error")
                        }

                        override fun onAdLoaded(p0: Ad?) {
                            nativeListener.onLoaded(this@apply)
                        }

                        override fun onAdClicked(p0: Ad?) {}
                        override fun onLoggingImpression(p0: Ad?) {}
                        override fun onMediaDownloaded(p0: Ad?) {}

                    })
                    .build()
            );
        }
    }
}