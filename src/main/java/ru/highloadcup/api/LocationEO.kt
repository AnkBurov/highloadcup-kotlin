package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonCreator

import javax.validation.constraints.Size

class LocationEO @JsonCreator constructor(props: Map<String, Any?>) {

    var place: String? = null

    @Size(max = 50)
    var country: String? = null

    @Size(max = 50)
    var city: String? = null

    var distance: Int? = null

    init {
        props.values.forEach { if (it == null) throw IllegalArgumentException() }
        this.place = props["place"] as String
        this.country = props["country"] as String
        this.city = props["city"] as String
        this.distance = props["distance"] as Int
    }
}
