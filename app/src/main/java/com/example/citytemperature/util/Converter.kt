package com.example.citytemperature.util

import javax.inject.Inject

class Converter @Inject constructor() {

    fun kelvinToCelsius(value: Float): Float {
        return value - 273.15f
    }
}