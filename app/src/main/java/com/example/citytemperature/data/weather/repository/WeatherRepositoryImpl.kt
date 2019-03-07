package com.example.citytemperature.data.weather.repository

import android.content.Context
import com.example.citytemperature.R
import com.example.citytemperature.data.weather.model.Weather
import com.example.citytemperature.data.weather.network.WeatherRequestManager
import com.example.citytemperature.domain.WeatherRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    val context: Context, val requestManager: WeatherRequestManager): WeatherRepository{

    val appId = context.getString(R.string.open_weather_map_app_id)

    override fun getWeather(lat: Float, lng: Float): Observable<Weather> {
        return requestManager
            .service.getWeather(appId, lat, lng)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
