package com.example.citytemperature.data.city.model

import com.example.citytemperature.data.weather.model.Weather

class City(val name: String, val lat: Float?, val lng: Float?, val population: Int?, val distance: Float?){
    var weather: Weather? = null
}