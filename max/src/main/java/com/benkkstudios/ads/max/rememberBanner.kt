package com.benkkstudios.ads.max

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.asLayoutParam


@Composable
internal fun rememberBanner(
    size: BannerSize,
    adUnitId: String,
    onAdFailedToLoad: () -> Unit
): MaxAdView {
    val activity = LocalContext.current as Activity
    val adView = MaxAdView(adUnitId, activity).apply {
        layoutParams = size.asLayoutParam(activity)
        setListener(object : MaxAdViewAdListener {
            override fun onAdLoaded(p0: MaxAd) {
            }

            override fun onAdDisplayed(p0: MaxAd) {
            }

            override fun onAdHidden(p0: MaxAd) {
            }

            override fun onAdClicked(p0: MaxAd) {
            }

            override fun onAdLoadFailed(p0: String, p1: MaxError) {
                onAdFailedToLoad.invoke()
            }

            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
            }

            override fun onAdExpanded(p0: MaxAd) {
            }

            override fun onAdCollapsed(p0: MaxAd) {
            }

        })
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

private fun getAdLifecycleObserver(adView: MaxAdView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> adView.destroy()
            else -> {}
        }
    }