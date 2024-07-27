package com.benkkstudios.ads.core

import com.benkkstudios.ads.admob.AdmobFactory
import com.benkkstudios.ads.admob.AdmobManagerFactory
import com.benkkstudios.ads.core.provider.ProviderModel
import com.benkkstudios.ads.iron.IronSourceFactory
import com.benkkstudios.ads.max.MaxFactory
import com.benkkstudios.ads.meta.MetaFactory
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.base.BaseFactory
import com.benkkstudios.ads.startapp.StartAppFactory
import com.benkkstudios.ads.unity.UnityFactory
import com.benkkstudios.ads.wortise.WortiseFactory
import com.benkkstudios.ads.yandex.YandexFactory

object FactoryGenerator {
    fun create(providerModel: ProviderModel): BaseFactory = when (providerModel.provider) {
        Provider.ADMOB -> AdmobFactory(providerModel.providerUnit)
        Provider.ADMOB_MANAGER -> AdmobManagerFactory(providerModel.providerUnit)
        Provider.APPLOVIN -> MaxFactory(providerModel.providerUnit)
        Provider.IRONSOURCE -> IronSourceFactory(providerModel.providerUnit)
        Provider.STARTAPP -> StartAppFactory(providerModel.providerUnit)
        Provider.YANDEX -> YandexFactory(providerModel.providerUnit)
        Provider.META -> MetaFactory(providerModel.providerUnit)
        Provider.WORTISE -> WortiseFactory(providerModel.providerUnit)
        Provider.UNITY -> UnityFactory(providerModel.providerUnit)
        Provider.DISABLE -> DisableFactory()
    }
}