package com.benkkstudios.ads.core.base

open class SingletonTriple<out T, in A, in B>(private val constructor: (A, B) -> T) {
    @Volatile
    private var instance: T? = null

    fun getInstance(a: A, b: B): T =
        instance ?: synchronized(this) {
            instance ?: constructor(a, b).also { instance = it }
        }
}

open class SingletonHolder<out T, in A>(private val constructor: (A) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T =
        instance ?: synchronized(this) {
            instance ?: constructor(arg).also { instance = it }
        }
}

open class SingletonHolderSingle<out T>(private val constructor: () -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(): T =
        instance ?: synchronized(this) {
            instance ?: constructor().also { instance = it }
        }
}