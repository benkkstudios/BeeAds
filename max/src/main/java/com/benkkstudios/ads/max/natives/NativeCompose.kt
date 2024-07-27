package com.benkkstudios.ads.max.natives

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.applovin.mediation.nativeAds.MaxNativeAd
import com.benkkstudios.ads.max.databinding.MaxNativeViewBinding
import com.benkkstudios.ads.max.interfaces.NativeListener
import com.benkkstudios.ads.shared.enums.NativeSize
import com.benkkstudios.ads.shared.enums.NativeSize.MEDIUM

@Composable
internal fun NativeCompose(adUnit: String, size: NativeSize, onAdFailedToLoad: () -> Unit) {
    val context = LocalContext.current
    var isAdRequested by remember { mutableStateOf(false) }
    var nativeAd by remember { mutableStateOf<MaxNativeAd?>(null) }
    AndroidViewBinding(
        factory = MaxNativeViewBinding::inflate,
        modifier = Modifier.wrapContentHeight(unbounded = true)
    ) {
        mediaView.visibility = if (size == MEDIUM) VISIBLE else GONE
        nativeAd?.let { bindNative(this, it) }
    }

    LaunchedEffect(nativeAd) {
        if (nativeAd == null && !isAdRequested) {
            kotlin.runCatching {
                NativeLoader(adUnit).load(context, object : NativeListener {
                    override fun onNativeAdLoaded(ad: MaxNativeAd?) {
                        nativeAd = ad
                    }

                    override fun onNativeAdLoadFailed(error: String) {
                        onAdFailedToLoad.invoke()
                        nativeAd = null
                    }
                })
            }.onSuccess {
                isAdRequested = true
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    nativeAd = null
                }

                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            nativeAd = null
        }
    }
}


private fun bindNative(binding: MaxNativeViewBinding, ad: MaxNativeAd) {
    with(binding) {
        ad.body?.let { body.text = it }
        ad.icon?.drawable.let { icon.setImageDrawable(it) }
        ad.starRating?.let { ratingBar.rating = it.toFloat() }
        ad.advertiser?.let { secondary.text = it }
        ad.callToAction?.let { cta.text = it }
        ad.mediaView?.let { mediaView.addView(it) }
        ad.title?.let { primary.text = it }
        root.setOnClickListener {
            ad.performClick()
        }
        root.isVisible = true
    }
}

