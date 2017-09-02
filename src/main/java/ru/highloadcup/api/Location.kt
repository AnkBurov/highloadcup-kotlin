package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Size
import java.io.Serializable

data class Location(
        @JsonProperty("id")
        val id: Int,

        @JsonProperty("place")
        val place: String,

        @JsonProperty("country")
        @Size(max = 50)
        val country: String,

        @JsonProperty("city")
        @Size(max = 50)
        val city: String,

        @JsonProperty("distance")
        val distance: Int
) : Serializable