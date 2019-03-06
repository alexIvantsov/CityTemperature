package com.example.citytemperature.service.location

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.example.citytemperature.service.location.exception.GpsSettingsChangeCancelledException
import com.example.citytemperature.service.location.exception.GpsSettingsChangesUnavailableException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class GpsStatusManager(val context: Context, val gpsResolutionProvider: GpsResolutionProvider) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var googleApiClient: GoogleApiClient? = null
    private var behaviorSubject = BehaviorSubject.create<Any>()
    val REQUEST_CODE_GPS_RESOLUTION = 2

    fun switchOnGps(): Observable<Any>? {
        connectToGoogleApiClient()
        return behaviorSubject
    }

    private fun connectToGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        googleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        val locationRequestHighAcurarcy = LocationRequest()
        locationRequestHighAcurarcy.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequestHighAcurarcy)
        builder.setAlwaysShow(true)

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback {
            switchOnGps(it.status)
        }
    }

    private fun switchOnGps(status: Status){
        when(status.statusCode){
            LocationSettingsStatusCodes.SUCCESS -> behaviorSubject.onNext(0)
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                gpsResolutionProvider
                    .startGpsResolution(status)
                    .doOnNext {
                        if (it != Activity.RESULT_OK) {
                            throw GpsSettingsChangeCancelledException()
                        }
                    }
                    .subscribe(behaviorSubject)
            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE ->
                behaviorSubject.onError(GpsSettingsChangesUnavailableException())
        }
    }

    fun gpsResolutionResult(requestCode: Int, resultCode: Int){
        if(requestCode != REQUEST_CODE_GPS_RESOLUTION) return
        if(resultCode == Activity.RESULT_OK){
            behaviorSubject.onNext(0)
        }else{
            behaviorSubject.onError(GpsSettingsChangeCancelledException())
        }
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }
}
