package com.example.citytemperature.presentation

import com.example.citytemperature.data.city.model.City
import com.example.citytemperature.domain.Interactor
import com.example.citytemperature.service.location.exception.GpsSettingsChangeCancelledException
import com.example.citytemperature.service.location.exception.GpsSettingsChangesUnavailableException
import com.example.citytemperature.service.location.exception.LocationPermissionNeverAskAgainException
import com.example.citytemperature.service.location.exception.LocationPermissionNotGrantedException
import com.example.citytemperature.util.Converter
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class CityListPresenter @Inject constructor(
    private var screen: CityListScreen?, private val interactor: Interactor, private val converter: Converter
) {

    var radius = 20
    private val compositeDisposable = CompositeDisposable()
    private var cities: List<City>? = null

    fun loadCities() {
        screen?.showProgressBar(true)
        screen?.hideErrors()
        compositeDisposable.add(
            interactor
                .getCityList(radius)
                .subscribe({
                    cities = it
                    screen?.showCities(it)
                    screen?.showProgressBar(false)
                }, {
                    screen?.showProgressBar(false)
                    handleError(it)
                })
        )
    }

    fun getWeather(city: City, position: Int) {
        compositeDisposable.add(interactor
            .getWeather(city)
            .map {
                val temp = it.main.temp
                val celsiusTemp = converter.kelvinToCelsius(temp)
                it.main.temp = celsiusTemp
                return@map it
            }
            .map { city.weather = it }
            .subscribe({ screen?.cityUpdated(position) }, {})
        )
    }

    fun openMapClicked() {
        if (cities != null) {
            screen?.showCitiesOnMap(ArrayList(cities))
        }
    }

    private fun handleError(e: Throwable) {
        if (e is ConnectException || e is SocketTimeoutException || e is UnknownHostException) {
            screen?.showNoInternetConnectionError()
        } else if (e is HttpException) {
            screen?.showServerError()
        } else if (e is GpsSettingsChangeCancelledException) {
            screen?.showGpsNotEnabledError()
        } else if (e is LocationPermissionNotGrantedException) {
            screen?.showLocationPermisssionsNotGrantedError()
        } else if (e is LocationPermissionNeverAskAgainException) {
            screen?.showNeverAskAgainError()
        } else if (e is GpsSettingsChangesUnavailableException) {
            screen?.showGpsUnavailableError()
        }
    }

    fun destroy() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        screen = null
    }
}