package com.example.citytemperature.dagger

import android.content.Context
import com.example.citytemperature.CityTemperature
import com.example.citytemperature.data.city.repository.CityRepositoryImpl
import com.example.citytemperature.data.weather.repository.WeatherRepositoryImpl
import com.example.citytemperature.domain.CityRepository
import com.example.citytemperature.domain.WeatherRepository
import dagger.Module
import dagger.Binds
import com.example.citytemperature.presentation.MainActivity
import com.example.citytemperature.presentation.MapActivity
import dagger.android.ContributesAndroidInjector

@Module
interface AppModule{

    @Binds
    fun bindContext(application: CityTemperature): Context

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun mainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    fun mapActivityInjector(): MapActivity

    @Binds
    fun cityRepositoryProvider(cityRepositroyImpl: CityRepositoryImpl): CityRepository

    @Binds
    fun weatherRepositoryProvider(weatherRepository: WeatherRepositoryImpl): WeatherRepository

}