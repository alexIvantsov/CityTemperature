package com.example.citytemperature.dagger

import android.content.Context

object Injector {

    lateinit var appComponent: AppComponent
        private set

    fun init(context: Context) {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(context)).build()
    }
}