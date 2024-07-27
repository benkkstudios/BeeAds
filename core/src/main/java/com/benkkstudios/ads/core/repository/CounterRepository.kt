package com.benkkstudios.ads.core.repository

internal object CounterRepository {
    private var INTERSTITIAL_COUNTER = 0
    fun plus(): Int = INTERSTITIAL_COUNTER++
    fun equal(interval: Int): Boolean = INTERSTITIAL_COUNTER == interval
    fun reset() = apply { INTERSTITIAL_COUNTER = 0 }
}