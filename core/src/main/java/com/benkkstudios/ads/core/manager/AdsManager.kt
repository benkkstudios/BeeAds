package com.benkkstudios.ads.core.manager

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.benkkstudios.ads.core.DisableFactory
import com.benkkstudios.ads.core.FactoryGenerator
import com.benkkstudios.ads.core.enums.ProviderType
import com.benkkstudios.ads.core.provider.ProviderModel
import com.benkkstudios.ads.core.repository.CounterRepository
import com.benkkstudios.ads.shared.BannerSize
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.shared.base.SingletonHolder
import com.benkkstudios.ads.shared.enums.NativeSize
import java.util.EnumMap
import java.util.concurrent.atomic.AtomicBoolean

internal class AdsManager(private val providers: EnumMap<ProviderType, ProviderModel>) {
    companion object : SingletonHolder<AdsManager, EnumMap<ProviderType, ProviderModel>>(::AdsManager)

    private val isInitialized = AtomicBoolean(false)
    private lateinit var _primaryManager: BaseFactory
    private lateinit var _secondaryManager: BaseFactory
    private val counterRepository: CounterRepository = CounterRepository

    private fun createProvider() {
        _primaryManager = providers[ProviderType.PRIMARY]?.let { FactoryGenerator.create(it) } ?: DisableFactory()
        _secondaryManager = providers[ProviderType.SECONDARY]?.let { FactoryGenerator.create(it) } ?: DisableFactory()
    }


    private fun initProvider(activity: Activity, onInitialized: (() -> Unit)?) {
        _primaryManager.init(activity) {
            _secondaryManager.init(activity, onInitialized)
        }
    }


    fun init(activity: Activity, application: Application?, onInitialized: (() -> Unit)?) {
        if (isInitialized.getAndSet(true)) {
            onInitialized?.invoke()
            return
        }
        createProvider()
        initProvider(activity, onInitialized)
        OpenAdsManager(_primaryManager, _secondaryManager).init(application)
    }

    fun showInterstitial(activity: Activity, callback: (() -> Unit)?) {
        _primaryManager.showInterstitial(activity, onSuccess = { callback?.invoke() }, onError = {
            _secondaryManager.showInterstitial(activity, onSuccess = { callback?.invoke() }, onError = { callback?.invoke() })
        })
    }

    fun showInterstitialWithCount(activity: Activity, interval: Int, callback: (() -> Unit)?) {
        if (interval == -1) {
            throw Exception("Interval not found, please call BeeAds.Builder(ctx).interval(5)")
        }
        counterRepository.plus()
        if (counterRepository.equal(interval)) {
            counterRepository.reset()
            showInterstitial(activity, callback)
        } else {
            callback?.invoke()
        }
    }

    fun showReward(activity: Activity, callback: (() -> Unit)?) {
        _primaryManager.showReward(activity, onSuccess = { callback?.invoke() }, onError = {
            _secondaryManager.showReward(activity, onSuccess = { callback?.invoke() }, onError = { callback?.invoke() })
        })
    }

    @Composable
    fun BannerCompose(size: BannerSize) {
        var firstNotLoaded by remember { mutableStateOf(false) }
        if (!firstNotLoaded) {
            _primaryManager.BannerCompose(size = size) {
                firstNotLoaded = true
            }
        }
        if (firstNotLoaded) {
            _secondaryManager.BannerCompose(size = size) {}
        }
    }

    @Composable
    fun NativeCompose(size: NativeSize) {
        var firstNotLoaded by remember { mutableStateOf(false) }
        if (!firstNotLoaded) {
            _primaryManager.NativeCompose(size = size) {
                firstNotLoaded = true
            }
        }
        if (firstNotLoaded) {
            _secondaryManager.NativeCompose(size = size) {}
        }
    }
}