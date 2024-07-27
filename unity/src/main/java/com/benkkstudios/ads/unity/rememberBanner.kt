package com.benkkstudios.ads.unity

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.BannerSize
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize


@Composable
fun rememberBanner(size: BannerSize, adUnitId: String, onAdFailedToLoad: () -> Unit): BannerView {
    val activity = LocalContext.current as Activity

    val adView = BannerView(activity, adUnitId, UnityBannerSize(size.width, size.height)).apply {
        listener = object : BannerView.Listener() {
            override fun onBannerFailedToLoad(bannerAdView: BannerView?, errorInfo: BannerErrorInfo?) {
                super.onBannerFailedToLoad(bannerAdView, errorInfo)
                onAdFailedToLoad.invoke()
            }
        }
        load()
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


private fun getAdLifecycleObserver(adView: BannerView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> adView.destroy()
            else -> {}
        }
    }