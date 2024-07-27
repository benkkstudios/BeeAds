package com.benkkstudios.ads.core

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.enums.NativeSize


/**
 * Show interstitial
 *
 * @param onSuccess
 */
fun Activity.showInterstitial(onSuccess: (() -> Unit)? = null) = BeeAds.showInterstitial(this, onSuccess)

/**
 * Show interstitial
 *
 * @param onSuccess
 */
fun Context.showInterstitial(onSuccess: (() -> Unit)? = null) = kotlin.runCatching {
    BeeAds.showInterstitial(this as Activity, onSuccess)
}.onFailure {
    throw Exception("please call showInterstitial in activity level")
}

/**
 * Show interstitial with count
 *
 * @param onSuccess
 */
fun Activity.showInterstitialWithCount(onSuccess: (() -> Unit)? = null) = BeeAds.showInterstitialWithCount(this, onSuccess)

/**
 * Show interstitial with count
 *
 * @param onSuccess
 */
fun Context.showInterstitialWithCount(onSuccess: (() -> Unit)? = null) = kotlin.runCatching {
    BeeAds.showInterstitialWithCount(this as Activity, onSuccess)
}.onFailure {
    throw Exception("please call showInterstitialWithCount in activity level")
}

/**
 * Show reward
 *
 * @param onSuccess
 */
fun Activity.showReward(onSuccess: (() -> Unit)? = null) = BeeAds.showReward(this, onSuccess)

/**
 * Show reward
 *
 * @param onSuccess
 */
fun Context.showReward(onSuccess: (() -> Unit)? = null) = kotlin.runCatching {
    BeeAds.showReward(this as Activity, onSuccess)
}.onFailure {
    throw Exception("please call showReward in activity level")
}

/**
 * Banner ad compose
 *
 * @param size
 */
@Composable
fun BannerAdCompose(size: BannerSize? = null) = BeeAds.BannerCompose(size)

/**
 * Native ad compose
 *
 * @param size
 */
@Composable
fun NativeAdCompose(size: NativeSize? = null) = BeeAds.NativeCompose(size)