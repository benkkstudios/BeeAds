package com.benkkstudios.ads.core

import com.benkkstudios.ads.shared.Provider


internal fun String.toProvider() = Provider.entries.firstOrNull { it.name == this } ?: Provider.DISABLE