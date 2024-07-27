package com.benkkstudios.ads.unity.natives

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.enums.NativeSize
import com.unity3d.services.banners.BannerErrorInfo
import com.unity3d.services.banners.BannerView
import com.unity3d.services.banners.UnityBannerSize

@Composable
fun rememberNative(adUnitId: String, size: NativeSize, onAdFailedToLoad: () -> Unit): BannerView {
    val activity = LocalContext.current as Activity
    val admobSize = if (size == NativeSize.MEDIUM) {
        BannerSize.MEDIUM_RECTANGLE
    } else {
        BannerSize.BANNER
    }
    val adView = BannerView(activity, adUnitId, UnityBannerSize(admobSize.width, admobSize.height)).apply {
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