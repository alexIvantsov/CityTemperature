package com.example.citytemperature.util

import android.content.Context
import com.example.citytemperature.R
import javax.inject.Inject
import kotlin.math.roundToInt

class Formatter @Inject constructor(val context: Context) {

    fun formatTemperature(value: Float): String {
        val temperatureString = value.roundToInt().toString()
        val sign = if (value > 0) "+" else ""
        return context.getString(R.string.temperature, (sign+temperatureString))
    }
}