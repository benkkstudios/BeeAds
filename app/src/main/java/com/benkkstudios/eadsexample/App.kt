package com.benkkstudios.eadsexample

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        @Volatile
        private var instance: App? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: App().also { instance = it }
            }
    }
}