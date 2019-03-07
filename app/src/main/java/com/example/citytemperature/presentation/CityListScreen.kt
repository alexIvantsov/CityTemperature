package com.example.citytemperature.presentation

import com.example.citytemperature.data.city.model.City

interface CityListScreen{

    fun showCities(cities: List<City>)
    fun cityUpdated(position: Int)
    fun showProgressBar(isShow: Boolean)
}