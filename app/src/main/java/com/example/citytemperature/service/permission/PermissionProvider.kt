package com.example.citytemperature.service.permission

import io.reactivex.Observable

interface PermissionProvider {
    fun requestPermission(permissions: Array<String>): Observable<PermissionResult>
    fun shouldShowRequestPermissionRationale(permission: String): Boolean
}