package com.benkkstudios.ads.wortise

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.BannerSize
import com.wortise.ads.AdError
import com.wortise.ads.AdSize
import com.wortise.ads.banner.BannerAd


@Composable
internal fun rememberBanner(
    size: BannerSize,
    adsUnitId: String,
    onAdFailedToLoad: () -> Unit
): BannerAd {
    val activity = LocalContext.current as Activity
    val adView = BannerAd(activity).apply {
        adSize = AdSize(size.width, size.height)
        adUnitId = adsUnitId
        listener = object : BannerAd.Listener {
            override fun onBannerFailedToLoad(ad: BannerAd, error: AdError) {
                super.onBannerFailedToLoad(ad, error)
                onAdFailedToLoad.invoke()
            }
        }
        loadAd()
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

private fun getAdLifecycleObserver(adView: BannerAd): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> adView.destroy()
            else -> {}
        }
    }