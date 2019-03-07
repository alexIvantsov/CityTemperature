package com.example.citytemperature.data.city.mapper

import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.data.city.model.CityResponse
import java.lang.NumberFormatException
import javax.inject.Inject

class CityDataMapper @Inject constructor() {

    fun from(cityResponse: CityResponse): City {
        val name = cityResponse.name ?: String()
        val lat: Float? = try {
            cityResponse.lat?.toFloat()
        } catch (e: NumberFormatException) {
            null
        }
        val lng: Float? = try {
            cityResponse.lng?.toFloat()
        } catch (e: NumberFormatException) {
            null
        }
        val distance: Float? = try {
            cityResponse.dist?.toFloat()
        } catch (e: NumberFormatException) {
            null
        }
        val population: Int? = try {
            cityResponse.population?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        return City(name, lat, lng, population, distance)
    }
}