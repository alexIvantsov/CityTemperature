package com.example.citytemperature.domain

import com.example.citytemperature.data.city.mapper.CityDataMapper
import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.data.city.model.CityListRequestParams
import io.reactivex.Observable
import javax.inject.Inject

class InteractorImpl @Inject constructor(
    val locationService: LocationService,
    val cityRepository: CityRepository,
    val weatherRepository: WeatherRepository,
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
                    .map { cityRepository -> mapper.from(cityRepository) }
                    .flatMap flatMapInner@ { city ->
                        return@flatMapInner if(city.lat == null || city.lng == null){
                            Observable.just(city)
                        }else {
                            weatherRepository
                                .getWeather(city.lat, city.lng)
                                .map {
                                    city.weather = it
                                    return@map city
                                }
                        }
                    }
                    .toList()
                    .toObservable()
            }
    }
}