package com.example.citytemperature.presentation

import com.example.citytemperature.domain.Interactor
import javax.inject.Inject

class CityListPresenter @Inject constructor(val screen: CityListScreen, val interactor: Interactor ){

    var radius = 20

    fun init(){
        interactor
            .getCityList(radius)
            .subscribe({

            }, {

            })
    }
}