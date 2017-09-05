package ru.highloadcup.api

import javax.validation.constraints.Size
import java.io.Serializable

data class Location(
        val id: Int,

        val place: String,

        @Size(max = 50)
        val country: String,

        @Size(max = 50)
        val city: String,

        val distance: Int
) : Serializable