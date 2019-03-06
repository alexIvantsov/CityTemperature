package com.example.citytemperature.data.city.network

import com.example.citytemperature.data.city.model.CityListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CityRequestService{

    @GET("ajax/get-all-cities-inside.php")
    fun getCityListInRadius(
        @Query("lat") lat: Float?,
        @Query("lng") lng: Float?,
        @Query("radius") radius: Int?
    ): Observable<CityListResponse>
}