package com.benkkstudios.ads.yandex.natives

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
import com.benkkstudios.ads.shared.enums.NativeSize
import com.benkkstudios.ads.yandex.databinding.YandexNativeViewBinding
import com.benkkstudios.ads.yandex.interfaces.NativeListener
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.NativeAdException
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder


@Suppress("UNUSED_PARAMETER")
@Composable
internal fun NativeCompose(adUnit: String, size: NativeSize, onAdFailedToLoad: () -> Unit) {
    val context = LocalContext.current
    var isAdRequested by remember { mutableStateOf(false) }
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    AndroidViewBinding(
        factory = YandexNativeViewBinding::inflate,
        modifier = Modifier.wrapContentHeight(unbounded = true)
    ) {
        nativeAd?.let {
            bindNative(this, it)
            root.isVisible = true
        }
    }
    LaunchedEffect(nativeAd) {
        runCatching {
            if (nativeAd == null && !isAdRequested) {
                NativeLoder.getInstance(adUnit).load(context, object : NativeListener() {
                    override fun onLoaded(ad: NativeAd) {
                        super.onLoaded(ad)
                        nativeAd = ad
                    }

                    override fun onAdFailedToLoad(error: String) {
                        super.onAdFailedToLoad(error)
                        onAdFailedToLoad.invoke()
                    }
                })
            }
        }.onSuccess {
            isAdRequested = true
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

private fun bindNative(binding: YandexNativeViewBinding, nativeAd: NativeAd) {
    val nativeAdViewBinder = binding.run {
        NativeAdViewBinder.Builder(adView)
            .setAgeView(age)
            .setBodyView(body)
            .setCallToActionView(callToAction)
            .setDomainView(domain)
            .setFaviconView(favicon)
            .setFeedbackView(feedback)
            .setIconView(icon)
            .setMediaView(media)
            .setPriceView(price)
            .setRatingView(rating)
            .setReviewCountView(reviewCount)
            .setSponsoredView(sponsored)
            .setTitleView(title)
            .setWarningView(warning)
            .build()
    }

    try {
        nativeAd.bindNativeAd(nativeAdViewBinder)
        binding.root.isVisible = true
    } catch (_: NativeAdException) {
    }
}