package com.example.citytemperature.data.city.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Inject

class CityRequestManager @Inject constructor(){

    val service: CityRequestService

    private val url = "https://www.freemaptools.com"
    private val logTag = "Http"

    private val logInterceptor = HttpLoggingInterceptor { Log.d(logTag, it) }

    private val headerInterceptor = Interceptor {
        val original = it.request()
        val requestBuilder = original.newBuilder()
        requestBuilder
            .header(
                "Referer",
                "https://www.freemaptools.com/find-cityResponses-and-towns-inside-radius.htm"
            )
            .method(original.method(), original.body())
        val request = requestBuilder.build()
        it.proceed(request)
    }

    init {
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.interceptors().add(logInterceptor)
        httpClient.interceptors().add(headerInterceptor)
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit.create(CityRequestService::class.java)
    }

}