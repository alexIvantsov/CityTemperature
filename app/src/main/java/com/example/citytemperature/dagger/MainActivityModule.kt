package com.example.citytemperature.dagger

import com.example.citytemperature.data.city.repository.CityRepositoryImpl
import com.example.citytemperature.domain.CityRepository
import com.example.citytemperature.domain.Interactor
import com.example.citytemperature.domain.InteractorImpl
import com.example.citytemperature.domain.LocationService
import com.example.citytemperature.presentation.MainActivity
import com.example.citytemperature.service.location.GpsResolutionProvider
import com.example.citytemperature.service.location.LocationServiceImpl
import com.example.citytemperature.service.permission.PermissionProvider
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {

    @Binds
    abstract fun permissionProvider(permissionProvider: MainActivity): PermissionProvider

    @Binds
    abstract fun gpsResolutionProvider(gpsResolutionProvider: MainActivity): GpsResolutionProvider

    @Binds
    abstract fun locationServiceProvider(locationServiceImpl: LocationServiceImpl): LocationService

    @Binds
    abstract fun interactorProvider(interactorImpl: InteractorImpl): Interactor
}