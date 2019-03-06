package com.example.citytemperature.domain

import com.example.citytemperature.data.city.mapper.CityDataMapper
import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.data.city.model.CityListRequestParams
import io.reactivex.Observable

class InteractorImpl(
    val locationService: LocationService,
    val cityRepository: CityRepository,
    val mapper: CityDataMapper
) : Interactor {

    override fun getCityList(radius: Int): Observable<List<City>> {
        return locationService
            .getUserLocation()
            .map {
                CityListRequestParams(it.latitude.toFloat(), it.longitude.toFloat(), radius)
            }
            .flatMap { cityRepository.getCityList(it) }
            .map { it.cityResponses }
            .flatMap {
                Observable
                    .fromIterable(it)
                    .map{mapper.from(it)}
                    .toList()
                    .toObservable()
            }
    }
}