package com.benkkstudios.ads.meta.natives

import android.view.View
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
import com.benkkstudios.ads.meta.databinding.MetaNativeViewBinding
import com.benkkstudios.ads.shared.enums.NativeSize
import com.benkkstudios.ads.shared.enums.NativeSize.MEDIUM
import com.facebook.ads.AdOptionsView
import com.facebook.ads.NativeAd


@Composable
internal fun NativeCompose(adUnit: String, size: NativeSize, onAdFailedToLoad: () -> Unit) {
    val context = LocalContext.current
    var isAdRequested by remember { mutableStateOf(false) }
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    LaunchedEffect(nativeAd) {
        if (nativeAd == null && !isAdRequested) {
            kotlin.runCatching {
                NativeLoder.getInstance(adUnit).load(context, object : NativeListener() {
                    override fun onAdFailedToLoad(error: String) {
                        super.onAdFailedToLoad(error)
                        onAdFailedToLoad.invoke()
                        nativeAd = null
                    }

                    override fun onLoaded(ad: NativeAd) {
                        super.onLoaded(ad)
                        nativeAd = ad
                    }
                })
            }.onSuccess {
                isAdRequested = true
            }
        }
    }
    AndroidViewBinding(
        factory = MetaNativeViewBinding::inflate,
        modifier = Modifier.wrapContentHeight(unbounded = true)
    ) {
        nativeAdMedia.visibility = if (size == MEDIUM) VISIBLE else GONE
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

private fun bindView(binding: MetaNativeViewBinding, nativeAd: NativeAd) {
    with(binding) {
        val context = binding.root.context
        nativeAd.unregisterView()
        val adOptionsView = AdOptionsView(context, nativeAd, nativeAdLayout)
        adChoicesContainer.removeAllViews()
        adChoicesContainer.addView(adOptionsView, 0)
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.visibility = if (nativeAd.hasCallToAction()) VISIBLE else View.INVISIBLE
        nativeAdCallToAction.text = nativeAd.adCallToAction
        sponsoredLabel.text = nativeAd.sponsoredTranslation
        val clickableViews: MutableList<View> = ArrayList()
        clickableViews.add(nativeAdTitle)
        clickableViews.add(nativeAdCallToAction)
        nativeAd.registerViewForInteraction(
            nativeAdLayout, nativeAdMedia, nativeAdIcon, clickableViews
        );
        nativeAdLayout.isVisible = true
    }
}

