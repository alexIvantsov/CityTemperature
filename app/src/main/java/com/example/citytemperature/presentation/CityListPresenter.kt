package com.example.citytemperature.presentation

import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.domain.Interactor
import com.example.citytemperature.util.Converter
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable



class CityListPresenter @Inject constructor(
    private val screen: CityListScreen, private val interactor: Interactor, private val converter: Converter){

    var radius = 20
    private val compositeDisposable = CompositeDisposable()

    fun init(){
        screen.showProgressBar(true)
        compositeDisposable.add(interactor
            .getCityList(radius)
            .subscribe({
                screen.showCities(it)
                screen.showProgressBar(false)
            }, {
                screen.showProgressBar(false)
            }))
    }

    fun getWeather(city: City, position: Int){
        compositeDisposable.add(interactor
            .getWeather(city)
            .map {
                val temp = it.main.temp
                val celsiusTemp = converter.kelvinToCelsius(temp)
                it.main.temp = celsiusTemp
                return@map it
            }
            .map { city.weather = it }
            .subscribe({screen.cityUpdated(position)}, {})
        )
    }

    fun destroy(){
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}