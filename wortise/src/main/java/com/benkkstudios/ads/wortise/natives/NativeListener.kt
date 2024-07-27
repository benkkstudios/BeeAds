package com.benkkstudios.ads.wortise.natives

import com.google.android.gms.ads.nativead.NativeAd

internal open class NativeListener {
    open fun onLoaded(ad: NativeAd) {}
    open fun onAdFailedToLoad(error: String) {}
}