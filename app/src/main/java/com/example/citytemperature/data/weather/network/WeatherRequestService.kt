package com.example.citytemperature.data.weather.network

import com.example.citytemperature.data.weather.model.Weather
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherRequestService {
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("APPID") appId: String,
        @Query("lat") lat: Float,
        @Query("lon") lng: Float
    ): Observable<Weather>

}