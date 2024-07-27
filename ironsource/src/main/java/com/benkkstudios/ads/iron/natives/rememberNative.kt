package com.benkkstudios.ads.iron.natives

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.enums.NativeSize
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener

@Composable
fun rememberNative(size: NativeSize, onAdFailedToLoad: () -> Unit): IronSourceBannerLayout {
    val activity = LocalContext.current as Activity
    val adView = if (size == NativeSize.MEDIUM) {
        IronSource.createBanner(activity, ISBannerSize.RECTANGLE)
    } else {
        IronSource.createBanner(activity, ISBannerSize.BANNER)
    }
    adView.levelPlayBannerListener = object : LevelPlayBannerListener {
        override fun onAdLoaded(p0: AdInfo?) {
        }

        override fun onAdLoadFailed(p0: IronSourceError?) {
            onAdFailedToLoad.invoke()
        }

        override fun onAdClicked(p0: AdInfo?) {
        }

        override fun onAdLeftApplication(p0: AdInfo?) {
        }

        override fun onAdScreenPresented(p0: AdInfo?) {
        }

        override fun onAdScreenDismissed(p0: AdInfo?) {
        }

    }
    IronSource.loadBanner(adView)

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, adView) {
        val lifecycleObserver = getAdLifecycleObserver(adView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            IronSource.destroyBanner(adView)
        }
    }

    return adView
}

private fun getAdLifecycleObserver(adView: IronSourceBannerLayout): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> IronSource.destroyBanner(adView)
            else -> {}
        }
    }