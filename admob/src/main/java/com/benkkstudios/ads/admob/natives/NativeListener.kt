package com.benkkstudios.ads.admob.natives

import com.google.android.gms.ads.nativead.NativeAd

internal interface NativeListener {
    fun onLoaded(ad: NativeAd)
    fun onAdFailedToLoad(error: String)
}