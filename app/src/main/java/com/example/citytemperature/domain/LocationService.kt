package com.example.citytemperature.domain

import android.location.Location
import io.reactivex.Observable

interface LocationService {
    fun getUserLocation(): Observable<Location>
}