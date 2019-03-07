package com.example.citytemperature.data.weather.model

import android.os.Parcel
import android.os.Parcelable

class Weather(val main: Main) : Parcelable {

    class Main(var temp: Float) : Parcelable {
        constructor(source: Parcel) : this(
            source.readFloat()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeFloat(temp)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Main> = object : Parcelable.Creator<Main> {
                override fun createFromParcel(source: Parcel): Main = Main(source)
                override fun newArray(size: Int): Array<Main?> = arrayOfNulls(size)
            }
        }
    }

    constructor(source: Parcel) : this(
        source.readParcelable<Main>(Main::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(main, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Weather> = object : Parcelable.Creator<Weather> {
            override fun createFromParcel(source: Parcel): Weather = Weather(source)
            override fun newArray(size: Int): Array<Weather?> = arrayOfNulls(size)
        }
    }
}
