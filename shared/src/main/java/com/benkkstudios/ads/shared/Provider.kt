package com.benkkstudios.ads.shared

enum class Provider(val factoryName: String) {
    ADMOB("AdmobFactory"),
    ADMOB_MANAGER("AdmobManagerFactory"),
    APPLOVIN("MaxFactory"),
    IRONSOURCE("IronSourceFactory"),
    STARTAPP("StartAppFactory"),
    YANDEX("YandexFactory"),
    META("MetaFactory"),
    WORTISE("WortiseFactory"),
    UNITY("UnityFactory"),
    DISABLE("DisableFactory"),
}
