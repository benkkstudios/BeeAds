package com.benkkstudios.ads.wortise

import android.app.Activity
import android.content.Context
import com.benkkstudios.ads.shared.AdsConstant.openAdsShowing
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.wortise.ads.AdError
import com.wortise.ads.appopen.AppOpenAd

class OpenAdLoader(private val unit: ProviderUnit) {
    companion object : SingletonHolder<OpenAdLoader, ProviderUnit>(::OpenAdLoader)

    private var appOpenAd: AppOpenAd? = null

    fun loadOpenAd(context: Context, onComplete: (() -> Unit)? = null) {
        appOpenAd = AppOpenAd(context, unit.open).apply {
            listener = object : AppOpenAd.Listener {
                override fun onAppOpenLoaded(ad: AppOpenAd) {
                    super.onAppOpenLoaded(ad)
                    onComplete?.invoke()
                }

                override fun onAppOpenFailedToLoad(ad: AppOpenAd, error: AdError) {
                    super.onAppOpenFailedToLoad(ad, error)
                    onComplete?.invoke()
                }
            }
            loadAd()
        }
    }


    fun showOpenAd(activity: Activity, onFailedToShow: (() -> Unit)?) {
        if (openAdsShowing.get()) {
            return
        }

        if (appOpenAd == null) {
            loadOpenAd(activity)
            return
        }
        appOpenAd?.run {
            if (isAvailable) {
                listener = object : AppOpenAd.Listener {
                    override fun onAppOpenFailedToShow(ad: AppOpenAd, error: AdError) {
                        super.onAppOpenFailedToShow(ad, error)
                        onFailedToShow?.invoke()
                        appOpenAd = null
                        openAdsShowing.set(false)
                        loadOpenAd(activity)
                    }

                    override fun onAppOpenDismissed(ad: AppOpenAd) {
                        super.onAppOpenDismissed(ad)
                        appOpenAd = null
                        openAdsShowing.set(false)
                        loadOpenAd(activity)
                    }

                    override fun onAppOpenShown(ad: AppOpenAd) {
                        super.onAppOpenShown(ad)
                        openAdsShowing.set(true)
                    }
                }
                showAd(activity)
            } else {
                onFailedToShow?.invoke()
            }

        }
    }
}