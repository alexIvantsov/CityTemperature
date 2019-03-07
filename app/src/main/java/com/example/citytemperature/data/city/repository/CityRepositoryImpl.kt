package com.example.citytemperature.data.city.repository

import com.example.citytemperature.data.city.model.CityListRequestParams
import com.example.citytemperature.data.city.model.CityListResponse
import com.example.citytemperature.data.city.network.CityRequestManager
import com.example.citytemperature.domain.CityRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(private val requestManager: CityRequestManager) :
    CityRepository {

    override fun getCityList(params: CityListRequestParams): Observable<CityListResponse> {
        return requestManager.service
            .getCityListInRadius(params.lat, params.lng, params.radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}