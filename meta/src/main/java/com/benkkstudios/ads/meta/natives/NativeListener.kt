package com.benkkstudios.ads.meta.natives

import com.facebook.ads.NativeAd


internal open class NativeListener {
    open fun onLoaded(ad: NativeAd) {}
    open fun onAdFailedToLoad(error: String) {}
}