package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

class UserVisit(
        val mark: Int,

        @JsonProperty("visited_at")
        val visitedAt: Long,

        val place: String
) : Serializable
