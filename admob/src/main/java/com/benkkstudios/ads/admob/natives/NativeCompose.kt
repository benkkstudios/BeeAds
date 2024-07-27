package com.benkkstudios.ads.admob.natives

import android.text.TextUtils
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
import com.benkkstudios.ads.admob.databinding.AdmobMediumNativeViewBinding
import com.benkkstudios.ads.shared.enums.NativeSize
import com.benkkstudios.ads.shared.enums.NativeSize.MEDIUM
import com.google.android.gms.ads.nativead.NativeAd


@Composable
internal fun NativeCompose(adUnit: String, size: NativeSize, onAdFailedToLoad: () -> Unit) {
    val context = LocalContext.current
    var isAdRequested by remember { mutableStateOf(false) }
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    LaunchedEffect(nativeAd) {
        if (nativeAd == null && !isAdRequested) {
            kotlin.runCatching {
                NativeLoader.getInstance(adUnit).load(context, object : NativeListener {
                    override fun onAdFailedToLoad(error: String) {
                        onAdFailedToLoad.invoke()
                        nativeAd = null
                    }

                    override fun onLoaded(ad: NativeAd) {
                        nativeAd = ad
                    }
                })
            }.onSuccess {
                isAdRequested = true
            }
        }
    }
    AndroidViewBinding(
        factory = AdmobMediumNativeViewBinding::inflate,
        modifier = Modifier.wrapContentHeight(unbounded = true)
    ) {
        mediaView.visibility = if (size == MEDIUM) VISIBLE else GONE
        nativeAd?.let { bindView(this, it) }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    nativeAd?.destroy()
                    nativeAd = null
                }

                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
            nativeAd?.destroy()
            nativeAd = null
        }
    }
}

private fun bindView(binding: AdmobMediumNativeViewBinding, ad: NativeAd) {
    with(binding) {
        val adView = nativeAdView.also { adview ->
            adview.callToActionView = nativeAdView
            adview.headlineView = primary
            adview.mediaView = mediaView
            adview.iconView = icon
        }
        ad.body?.let { body.text = it }
        ad.callToAction?.let { cta.text = it }
        ad.headline?.let { primary.text = it }
        ad.icon?.let { icon.setImageDrawable(it.drawable) }
        val secondaryText: String = if (adHasOnlyStore(ad)) {
            nativeAdView.storeView = secondary
            ad.store ?: ""
        } else {
            if (!TextUtils.isEmpty(ad.advertiser)) {
                nativeAdView.advertiserView = secondary
                ad.advertiser ?: ""
            }
            ""
        }
        ad.starRating?.let { starRating ->
            if (starRating > 0) {
                secondary.visibility = GONE
                ratingBar.visibility = VISIBLE
                ratingBar.rating = starRating.toFloat()
                adView.starRatingView = ratingBar
            } else {
                secondary.text = secondaryText
                secondary.visibility = VISIBLE
                ratingBar.visibility = GONE
            }
        }

        adView.setNativeAd(ad)
        adView.isVisible = true
    }
}

private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
    val store: String = nativeAd.store.toString()
    val advertiser: String = nativeAd.advertiser.toString()
    return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
}