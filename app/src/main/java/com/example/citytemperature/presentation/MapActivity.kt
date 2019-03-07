package com.example.citytemperature.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.citytemperature.R
import com.example.citytemperature.data.city.model.City
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlin.math.roundToInt
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.CameraUpdateFactory

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {

        const val INTENT_KEY_CITIES = "cities"

        fun startActivity(context: Context, cities: ArrayList<City>?) {
            val intent = Intent(context, MapActivity::class.java)
            cities?.let { intent.putParcelableArrayListExtra(INTENT_KEY_CITIES, cities) }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        if (map == null) return
        val cities = getCities() ?: return
        val positions = ArrayList<LatLng>()
        cities.forEach {
            val temperature = it.weather?.main?.temp
            val title = if (temperature == null) {
                it.name
            } else {
                val formattedTemperature = getString(R.string.temperature, temperature.roundToInt().toString())
                "${it.name} $formattedTemperature"
            }
            if (it.lat != null && it.lng != null) {
                val latLng = LatLng(it.lat.toDouble(), it.lng.toDouble())
                positions.add(latLng)
                val markerOptions = MarkerOptions().position(latLng).title(title)
                val marker = map.addMarker(markerOptions)
                marker.showInfoWindow()
            }
        }
        zoomMap(map, positions)
    }

    private fun zoomMap(map: GoogleMap, positions: List<LatLng>) {
        val builder = LatLngBounds.Builder()
        positions.forEach { builder.include(it) }
        val bounds = builder.build()
        val padding = 0
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        map.animateCamera(cameraUpdate)
    }

    private fun getCities(): List<City>? {
        if (!intent.hasExtra(INTENT_KEY_CITIES)) return null
        return intent.getParcelableArrayListExtra(INTENT_KEY_CITIES)
    }
}