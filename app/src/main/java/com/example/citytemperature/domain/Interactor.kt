package com.example.citytemperature.domain

import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.data.weather.model.Weather
import io.reactivex.Observable
import io.reactivex.Single

interface Interactor{
    fun getCityList(radius: Int): Observable<List<City>>
    fun getWeather(city: City): Observable<Weather>
}