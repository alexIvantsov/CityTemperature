package com.example.citytemperature.domain

import com.example.citytemperature.data.city.mapper.CityDataMapper
import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.data.city.model.CityListRequestParams
import com.example.citytemperature.data.weather.model.Weather
import com.example.citytemperature.util.Converter
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
                    .map { mapper.from(it) }
                    .toList()
                    .toObservable()
            }
    }

    override fun getWeather(city: City): Observable<Weather>{
        return if(city.lat == null || city.lng == null){
            Observable.empty()
        }else {
            weatherRepository.getWeather(city.lat, city.lng)
        }
    }
}