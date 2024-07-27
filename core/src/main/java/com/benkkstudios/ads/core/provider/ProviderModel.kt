package com.benkkstudios.ads.core.provider

import androidx.compose.runtime.Stable
import com.benkkstudios.ads.shared.Provider
import com.benkkstudios.ads.shared.ProviderUnit

@Stable
data class ProviderModel(
    val provider: Provider = Provider.DISABLE,
    val providerUnit: ProviderUnit = ProviderUnit(),
)