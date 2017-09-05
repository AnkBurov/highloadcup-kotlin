package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import java.io.Serializable

data class Visit(
        val id: Int,

        @JsonProperty("location")
        val locationId: Int,

        @JsonProperty("user")
        val userId: Int,

        @Min(946684800L)
        @Max(1420070400L)
        @JsonProperty("visited_at")
        val visitedAt: Long,

        @Min(0)
        @Max(5)
        var mark: Int
) : Serializable
