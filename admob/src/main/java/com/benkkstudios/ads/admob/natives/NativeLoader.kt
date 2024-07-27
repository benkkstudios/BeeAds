package com.benkkstudios.ads.admob.natives

import android.content.Context
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class NativeLoader(private val adUnit: String) {

    companion object : SingletonHolder<NativeLoader, String>(::NativeLoader)

    fun load(context: Context, nativeListener: NativeListener) {
        CoroutineScope(Dispatchers.IO).launch {
            AdLoader.Builder(context, adUnit)
                .forNativeAd { ad ->
                    nativeListener.onLoaded(ad)
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        super.onAdFailedToLoad(error)
                        nativeListener.onAdFailedToLoad(error.message)
                    }
                })
                .build().also {
                    it.loadAds(AdRequest.Builder().build(), 1)
                }
        }

    }
}