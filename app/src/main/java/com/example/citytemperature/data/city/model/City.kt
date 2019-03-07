package com.example.citytemperature.data.city.model

import android.os.Parcel
import android.os.Parcelable
import com.example.citytemperature.data.weather.model.Weather

class City(
    val name: String,
    val lat: Float?,
    val lng: Float?,
    val population: Int?,
    val distance: Float?,
    var weather: Weather? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readValue(Float::class.java.classLoader) as Float?,
        source.readValue(Float::class.java.classLoader) as Float?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Float::class.java.classLoader) as Float?,
        source.readParcelable<Weather>(Weather::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeValue(lat)
        writeValue(lng)
        writeValue(population)
        writeValue(distance)
        writeParcelable(weather, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<City> = object : Parcelable.Creator<City> {
            override fun createFromParcel(source: Parcel): City = City(source)
            override fun newArray(size: Int): Array<City?> = arrayOfNulls(size)
        }
    }
}