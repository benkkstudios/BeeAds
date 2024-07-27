package com.benkkstudios.ads.core

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.Composable
import com.benkkstudios.ads.core.enums.ProviderType
import com.benkkstudios.ads.core.manager.AdsManager
import com.benkkstudios.ads.core.provider.ProviderModel
import com.benkkstudios.ads.shared.AdsConstant
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit
import com.benkkstudios.ads.shared.enums.NativeSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.EnumMap


/**
 * Bee ads
 *
 * @property builder
 * @constructor
 *
 * @param activity
 */
class BeeAds(activity: Activity, private val builder: Builder) {
    companion object {
        var isInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)
        private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
        private lateinit var adsManager: AdsManager
        private var interval: Int = -1

        /**
         * Show interstitial
         *
         * @param activity
         * @param onSuccess
         */
        fun showInterstitial(activity: Activity, onSuccess: (() -> Unit)? = null) {
            adsManager.showInterstitial(activity, onSuccess)
        }

        /**
         * Show interstitial with count
         *
         * @param activity
         * @param onSuccess
         */
        fun showInterstitialWithCount(activity: Activity, onSuccess: (() -> Unit)? = null) {
            adsManager.showInterstitialWithCount(activity, interval, onSuccess)
        }

        /**
         * Show reward
         *
         * @param activity
         * @param onSuccess
         */
        fun showReward(activity: Activity, onSuccess: (() -> Unit)? = null) {
            adsManager.showReward(activity, onSuccess)
        }

        /**
         * Banner compose
         *
         * @param size
         */
        @Composable
        fun BannerCompose(size: BannerSize? = null) {
            adsManager.BannerCompose(size ?: BannerSize.ADAPTIVE)
        }

        /**
         * Native compose
         *
         * @param size
         */
        @Composable
        fun NativeCompose(size: NativeSize? = null) {
            adsManager.NativeCompose(size = size ?: NativeSize.MEDIUM)
        }
    }

    init {
        if (!isInitialized.value) {
            BannerSize.createAdaptive(activity)
            interval = builder.interval
            adsManager = AdsManager.getInstance(builder.providers).apply {
                init(activity, builder.application) {
                    scope.launch { isInitialized.emit(true) }
                }
            }

        }
    }

    /**
     * Bulder
     *
     * @property activity
     * @constructor Create empty Builder
     */
    class Builder(private val activity: Activity) {
        val providers: EnumMap<ProviderType, ProviderModel> = EnumMap(ProviderType::class.java)
        var application: Application? = null
        var interval: Int = -1

        /**
         * Primary provider
         *
         * @param provider
         */
        fun primaryProvider(provider: Any, providerUnit: ProviderUnit) = apply {
            providers[ProviderType.PRIMARY] = if (provider is String) {
                ProviderModel(provider.toProvider(), providerUnit)
            } else {
                ProviderModel(provider as Provider, providerUnit)
            }
        }

        /**
         * Second provider for ads backups
         *
         * @param provider
         */
        fun secondProvider(provider: Any, providerUnit: ProviderUnit) = apply {
            providers[ProviderType.SECONDARY] = if (provider is String) {
                ProviderModel(provider.toProvider(), providerUnit)
            } else {
                ProviderModel(provider as Provider, providerUnit)
            }
        }

        /**
         * App open
         *
         * @param application
         */
        fun appOpen(application: Application) = apply { this.application = application }

        /**
         * Debug mode will force test ads
         *
         * @param debugMode
         */
        fun debugMode(debugMode: Boolean) = apply { AdsConstant.DEBUG_MODE = debugMode }

        /**
         * Interval for interstitial
         *
         * @param interval
         */
        fun interval(interval: Int) = apply { this.interval = interval }

        /**
         * Build
         *
         * @return
         */
        fun build(): BeeAds = BeeAds(activity, this)
    }
}