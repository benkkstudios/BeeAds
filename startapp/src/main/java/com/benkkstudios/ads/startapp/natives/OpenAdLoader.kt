package com.benkkstudios.ads.startapp.natives

import android.app.Activity
import android.content.Context
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.base.SingletonHolderSingle
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener

class OpenAdLoader {
    companion object : SingletonHolderSingle<OpenAdLoader>(::OpenAdLoader)

    private var interstitalAd: StartAppAd? = null

    fun loadOpenAd(context: Context, onComplete: (() -> Unit)? = null) {
        interstitalAd = StartAppAd(context)
        interstitalAd?.loadAd(StartAppAd.AdMode.FULLPAGE, object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                onComplete?.invoke()
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
                onComplete?.invoke()
            }
        })
    }


    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (openAdsShowing.get()) {
            return
        }

        if (interstitalAd == null) {
            loadOpenAd(activity)
            return
        }
        interstitalAd?.showAd(object : AdDisplayListener {
            override fun adHidden(ad: Ad) {
                openAdsShowing.set(false)
                loadOpenAd(activity)
            }

            override fun adDisplayed(ad: Ad) {
                openAdsShowing.set(true)
            }

            override fun adClicked(ad: Ad) {
            }

            override fun adNotDisplayed(ad: Ad) {
                onFailedToShow?.invoke()
                openAdsShowing.set(false)
                loadOpenAd(activity)
            }
        })
    }


}