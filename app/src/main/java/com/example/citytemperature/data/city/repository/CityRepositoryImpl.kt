package com.example.citytemperature.data.city.repository

import com.example.citytemperature.data.city.model.CityListRequestParams
import com.example.citytemperature.data.city.model.CityListResponse
import com.example.citytemperature.data.city.network.CityRequestService
import com.example.citytemperature.domain.CityRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CityRepositoryImpl(private val requestService: CityRequestService) :
    CityRepository {

    override fun getCityList(params: CityListRequestParams): Observable<CityListResponse> {
        return requestService
            .getCityListInRadius(params.lat, params.lng, params.radius)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}