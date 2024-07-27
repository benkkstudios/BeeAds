package com.benkkstudios.ads.meta

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.BannerSize
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdListener
import com.facebook.ads.AdSize
import com.facebook.ads.AdView


@Suppress("UNUSED_PARAMETER")
@Composable
internal fun rememberBanner(
    size: BannerSize,
    adUnitId: String,
    onAdFailedToLoad: () -> Unit
): AdView {
    val activity = LocalContext.current as Activity
    val adView = AdView(activity, adUnitId, AdSize.BANNER_HEIGHT_50).apply {
        loadAd(buildLoadAdConfig().withAdListener(object : AdListener {
            override fun onError(p0: Ad?, error: AdError?) {
                onAdFailedToLoad.invoke()
            }

            override fun onAdLoaded(p0: Ad?) {
            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        }).build())
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

private fun getAdLifecycleObserver(adView: AdView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> adView.destroy()
            else -> {}
        }
    }