package com.benkkstudios.ads.yandex.interfaces

import com.yandex.mobile.ads.nativeads.NativeAd

internal open class NativeListener {
    open fun onLoaded(ad: NativeAd) {}
    open fun onAdFailedToLoad(error: String) {}
}