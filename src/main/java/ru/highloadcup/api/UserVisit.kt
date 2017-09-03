package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

class UserVisit(
        @JsonProperty("mark")
        val mark: Int,

        @JsonProperty("visited_at")
        val visited_at: Long,

        @JsonProperty("place")
        val place: String
) : Serializable
