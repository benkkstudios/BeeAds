package com.benkkstudios.ads.core

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.enums.NativeSize


class DisableFactory : BaseFactory(ProviderUnit()) {
    override fun setCompany() = Provider.DISABLE

    override fun init(activity: Activity, onInitializationComplete: (() -> Unit)?) {
        onInitializationComplete?.invoke()
    }

    override fun loadInterstitial(activity: Activity) {
    }

    override fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        onSuccess?.invoke()
    }

    override fun loadReward(activity: Activity) {
    }

    override fun showReward(activity: Activity, onSuccess: (() -> Unit)?, onError: ((String) -> Unit)?) {
        onSuccess?.invoke()
    }

    override fun loadOpenAd(context: Context, onComplete: (() -> Unit)?) {
        onComplete?.invoke()
    }

    override fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        onFailedToShow?.invoke()
    }

    @Composable
    override fun BannerCompose(size: BannerSize, onAdFailedToLoad: () -> Unit) {
    }

    @Composable
    override fun NativeCompose(size: NativeSize, onAdFailedToLoad: () -> Unit) {
    }

}