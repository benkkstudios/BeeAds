package com.benkkstudios.ads.yandex.interfaces

import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

open class BannerListener : BannerAdEventListener {
    override fun onAdLoaded() {
    }

    override fun onAdFailedToLoad(error: AdRequestError) {
    }

    override fun onAdClicked() {
    }

    override fun onLeftApplication() {
    }

    override fun onReturnedToApplication() {
    }

    override fun onImpression(impressionData: ImpressionData?) {
    }
}