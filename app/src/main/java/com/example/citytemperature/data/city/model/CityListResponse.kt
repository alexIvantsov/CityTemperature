package com.example.citytemperature.data.city.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "cityResponses", strict = false)
class CityListResponse {

    @field:ElementList(name = "city", inline = true)
    var cityResponses: List<CityResponse>? = null
}