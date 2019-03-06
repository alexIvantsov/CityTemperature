package com.example.citytemperature.dagger

import android.content.Context
import com.example.citytemperature.data.city.network.RequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context){

    @Provides
    @Singleton
    fun provideRequestManager() = RequestManager().service

    @Provides
    @Singleton
    fun provideContext() = context
}