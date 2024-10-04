package com.example.test

import android.app.Application
import com.example.test.manualdi.DefaultAppContainer

class TestApplication : Application() {
    private var _appContainer: DefaultAppContainer? = null
    val appContainer: DefaultAppContainer
        get() = checkNotNull(_appContainer)

    override fun onCreate() {
        super.onCreate()
        _appContainer = DefaultAppContainer(context = this.applicationContext)
    }
}