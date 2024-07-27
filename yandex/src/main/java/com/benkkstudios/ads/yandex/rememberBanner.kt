package com.benkkstudios.ads.yandex

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.yandex.interfaces.BannerListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError

@Composable
internal fun rememberBanner(
    size: BannerSize,
    adUnitId: String,
    onAdFailedToLoad: () -> Unit
): BannerAdView {
    val activity = LocalContext.current as Activity
    val adView = BannerAdView(activity).apply {
        setAdUnitId(adUnitId)
        setBannerAdEventListener(object : BannerListener() {
            override fun onAdFailedToLoad(error: AdRequestError) {
                super.onAdFailedToLoad(error)
                onAdFailedToLoad.invoke()
            }
        })
        setAdSize(BannerAdSize.inlineSize(activity, size.width, size.height))
        loadAd(AdRequest.Builder().build())
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, adView) {
        val lifecycleObserver = getAdLifecycleObserver(adView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            adView.destroy()
        }
    }

    return adView
}

private fun getAdLifecycleObserver(adView: BannerAdView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> adView.destroy()
            else -> {}
        }
    }