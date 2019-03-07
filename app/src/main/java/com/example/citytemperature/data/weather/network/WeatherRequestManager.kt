package com.example.citytemperature.data.weather.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class WeatherRequestManager @Inject constructor() {
    val service: WeatherRequestService

    private val url = "https://api.openweathermap.org/"
    private val logTag = "Http"

    private val logInterceptor = HttpLoggingInterceptor { Log.d(logTag, it) }
//    private val headerInterceptor = Interceptor {
    //        val original = it.request()
//        val requestBuilder = original.newBuilder()
//        requestBuilder
//            .header(
//                "Referer",
//                "https://www.freemaptools.com/find-cityResponses-and-towns-inside-radius.htm"
//            )
//            .method(original.method(), original.body())
//        val request = requestBuilder.build()
//        it.proceed(request)
//    }
//

    init {
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().add(logInterceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit.create(WeatherRequestService::class.java)
    }
}