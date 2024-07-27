package com.benkkstudios.ads.startapp

import android.annotation.SuppressLint
import com.startapp.sdk.ads.nativead.NativeAdDetails

internal abstract class NativeListener {
    @SuppressLint("ComposableNaming")
    abstract fun onLoaded(ad: NativeAdDetails)
    abstract fun onAdFailedToLoad(error: String)
}