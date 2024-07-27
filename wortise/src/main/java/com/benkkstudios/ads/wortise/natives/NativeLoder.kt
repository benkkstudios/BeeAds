package com.benkkstudios.ads.wortise.natives

import android.content.Context
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.google.android.gms.ads.nativead.NativeAd
import com.wortise.ads.AdError
import com.wortise.ads.natives.GoogleNativeAd

internal class NativeLoder(private val adUnit: String) {
    companion object : SingletonHolder<NativeLoder, String>(::NativeLoder)

    fun load(context: Context, nativeListener: NativeListener) {
        GoogleNativeAd(
            context, adUnit, object : GoogleNativeAd.Listener {
                override fun onNativeLoaded(ad: GoogleNativeAd, nativeAd: NativeAd) {
                    nativeListener.onLoaded(nativeAd)
                }

                override fun onNativeFailedToLoad(ad: GoogleNativeAd, error: AdError) {
                    super.onNativeFailedToLoad(ad, error)
                    nativeListener.onAdFailedToLoad(error.message)
                }
            }
        ).also {
            it.load()
        }
    }
}