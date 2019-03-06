package com.example.citytemperature.service.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.example.citytemperature.service.location.exception.LocationPermissionNeedExplainException
import com.example.citytemperature.service.location.exception.LocationPermissionNotGrantedException
import com.example.citytemperature.service.permission.PermissionResult
import com.example.citytemperature.service.permission.PermissionStatus
import com.example.citytemperature.service.permission.PermissionProvider
import io.reactivex.Observable

class LocationPermissionManager(private val context: Context, private val permissionProvider: PermissionProvider) {

    private val permissions =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    fun requestPermissions(): Observable<PermissionStatus> {
        return Observable.just(isPermissionGranted())
            .flatMap { permissionIsGranted ->
                return@flatMap if (permissionIsGranted) {
                    Observable.just(PermissionStatus.GRANTED)
                } else {
                    permissionProvider
                        .requestPermission(permissions)
                        .map { getPermissionResult(it) }
                        .map { checkPermissionStatus(it) }
                }
            }
    }

    private fun checkPermissionStatus(status: PermissionStatus): PermissionStatus {
        return when (status) {
            PermissionStatus.GRANTED -> status
            PermissionStatus.NOT_GRANTED -> throw LocationPermissionNotGrantedException()
            PermissionStatus.NEED_EXPLAIN -> throw  LocationPermissionNeedExplainException()
        }
    }

    private fun getPermissionResult(permissionResult: PermissionResult): PermissionStatus {
        var grantedPermissionsCount = 0
        for (i in permissions.indices) {
            val permission = permissions[i]
            val grantResult = permissionResult.grantResults[i]
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                grantedPermissionsCount++
            } else {
                val showRationale = permissionProvider.shouldShowRequestPermissionRationale(permission)
                if (!showRationale) {
                    return PermissionStatus.NEED_EXPLAIN
                }
            }
        }
        return if (grantedPermissionsCount == permissionResult.grantResults.size) {
            PermissionStatus.GRANTED
        } else {
            PermissionStatus.NOT_GRANTED
        }
    }

    private fun isPermissionGranted(): Boolean {
        var result = true
        val iterator = permissions.iterator()
        while (result && iterator.hasNext()) {
            val permission = iterator.next()
            result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        return result
    }

}