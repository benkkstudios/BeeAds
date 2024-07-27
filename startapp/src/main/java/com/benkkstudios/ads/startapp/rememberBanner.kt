package com.benkkstudios.ads.startapp

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.asLayoutParam
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener


@Composable
internal fun rememberBanner(
    size: BannerSize,
    onAdFailedToLoad: () -> Unit
): FrameLayout {
    val activity = LocalContext.current as Activity
    val frameLayout = FrameLayout(activity)
    Banner(activity).apply {
        setBannerListener(object : BannerListener {
            override fun onReceiveAd(view: View) {
                val bannerParameters = size.asLayoutParam(activity)
                frameLayout.addView(this@apply, bannerParameters)
            }

            override fun onFailedToReceiveAd(view: View) {
                onAdFailedToLoad.invoke()
            }

            override fun onImpression(view: View) {
            }

            override fun onClick(view: View) {
            }
        })
        loadAd()
    }


    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, frameLayout) {
        val lifecycleObserver = getAdLifecycleObserver(frameLayout)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            frameLayout.removeAllViews()
        }
    }

    return frameLayout
}

private fun getAdLifecycleObserver(frameLayout: FrameLayout): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> frameLayout.removeAllViews()
            else -> {}
        }
    }