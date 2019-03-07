package com.example.citytemperature.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.example.citytemperature.domain.LocationService
import io.reactivex.Observable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import com.google.android.gms.location.LocationRequest
import javax.inject.Inject


class LocationServiceImpl @Inject constructor(
    context: Context,
    private val permissionManager: LocationPermissionManager,
    private val gpsSwitchManager: GpsStatusManager
) : LocationService {

    private val locationProvider = ReactiveLocationProvider(context)
    private val request = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setNumUpdates(1)
        .setInterval(100)

    @SuppressLint("MissingPermission")
    override fun getUserLocation(): Observable<Location> {
        return permissionManager.requestPermissions()
            .flatMap { gpsSwitchManager.switchOnGps() }
            .flatMap { locationProvider.getUpdatedLocation(request) }
    }

}