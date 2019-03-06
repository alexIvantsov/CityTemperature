package com.example.citytemperature.service.location

import com.google.android.gms.common.api.Status
import io.reactivex.Observable

interface GpsResolutionProvider {
    fun startGpsResolution(status: Status): Observable<Int>
}