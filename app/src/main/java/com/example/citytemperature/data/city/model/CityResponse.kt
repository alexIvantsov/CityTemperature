package com.example.citytemperature.data.city.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "city", strict = true)
class CityResponse {
    @field:Attribute(name = "name", required = false)
    var name: String? = null
    @field:Attribute(name = "population", required = false)
    var population: String? = null
    @field:Attribute(name = "dist", required = false)
    var dist: String? = null
    @field:Attribute(name = "lat", required = false)
    var lat: String? = null
    @field:Attribute(name = "lng", required = false)
    var lng: String? = null
}