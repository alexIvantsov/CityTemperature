package com.example.citytemperature

import android.app.Application
import com.example.citytemperature.dagger.Injector

class CityTemperature : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}