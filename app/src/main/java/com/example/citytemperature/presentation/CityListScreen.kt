package com.example.citytemperature.presentation

import com.example.citytemperature.data.city.model.City

interface CityListScreen{

    fun showCities(cities: List<City>)
    fun cityUpdated(position: Int)
    fun showProgressBar(isShow: Boolean)
    fun showCitiesOnMap(cities: ArrayList<City>)
    fun showNoInternetConnectionError()
    fun showServerError()
    fun showGpsNotEnabledError()
    fun showLocationPermisssionsNotGrantedError()
    fun showNeverAskAgainError()
    fun showGpsUnavailableError()
    fun hideErrors()
}