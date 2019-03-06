package com.example.citytemperature.domain

import com.example.citytemperature.data.city.model.City
import io.reactivex.Observable
import io.reactivex.Single

interface Interactor{
    fun getCityList(radius: Int): Observable<List<City>>
}