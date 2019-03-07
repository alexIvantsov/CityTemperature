package com.example.citytemperature.presentation

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.citytemperature.R
import com.example.citytemperature.domain.Interactor
import com.example.citytemperature.service.location.GpsResolutionProvider
import com.example.citytemperature.service.permission.PermissionProvider
import com.example.citytemperature.service.permission.PermissionResult
import com.google.android.gms.common.api.Status
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), PermissionProvider,
    GpsResolutionProvider {

    private val REQUEST_CODE_PERMISSION = 1
    private val REQUEST_CODE_GPS_RESOLUTION = 2
    private var gpsResolutionObservable: BehaviorSubject<Int>? = null
    private var permissionObservable: BehaviorSubject<PermissionResult>? = null

    @Inject lateinit var interactor: Interactor

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val interactor: Interactor =
//            InteractorImpl(
//                LocationServiceImpl(
//                    this,
//                    LocationPermissionManager(this, this),
//                    GpsStatusManager(this, this)
//                ),
//                CityRepositoryImpl(CityRequestManager().service), CityDataMapper()
//            )
        interactor
            .getCityList(20)
            .subscribe({
                textView.text = it.first().name+" "+it.first().weather?.main?.temp
            }, { it.printStackTrace() })

    }

    override fun requestPermission(permissions: Array<String>): Observable<PermissionResult> {
        permissionObservable = BehaviorSubject.create<PermissionResult>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissions(permissions, REQUEST_CODE_PERMISSION)
        }
        return permissionObservable!!
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_PERMISSION) {
            val permissionResult =
                PermissionResult(requestCode, permissions, grantResults)
            permissionObservable?.onNext(permissionResult)
            permissionObservable?.onComplete()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun startGpsResolution(status: Status): Observable<Int> {
        gpsResolutionObservable = BehaviorSubject.create()
        status.startResolutionForResult(this, REQUEST_CODE_GPS_RESOLUTION)
        return gpsResolutionObservable!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GPS_RESOLUTION) {
            gpsResolutionObservable?.onNext(resultCode)
            gpsResolutionObservable?.onComplete()
        }
    }
}
