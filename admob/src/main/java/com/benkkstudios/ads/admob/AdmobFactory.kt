package com.benkkstudios.ads.admob

import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit

class AdmobFactory(unit: ProviderUnit) : AdmobFactoryImpl(unit) {
    override fun setCompany(): Provider = Provider.ADMOB
}