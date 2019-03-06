package com.example.citytemperature.domain

import com.example.citytemperature.data.city.model.CityListRequestParams
import com.example.citytemperature.data.city.model.CityListResponse
import io.reactivex.Observable

interface CityRepository{
    fun getCityList(params: CityListRequestParams): Observable<CityListResponse>
}