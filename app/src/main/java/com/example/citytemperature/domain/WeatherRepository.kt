package com.example.citytemperature.domain

import com.example.citytemperature.data.weather.model.Weather
import io.reactivex.Observable

interface WeatherRepository{
    fun getWeather(lat: Float, lng: Float): Observable<Weather>
}