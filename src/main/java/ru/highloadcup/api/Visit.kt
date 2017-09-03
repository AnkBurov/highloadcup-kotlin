package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import java.io.Serializable

data class Visit(
        @JsonProperty("id")
        val id: Int,

        @JsonProperty("location")
        val location: Int,

        @JsonProperty("user")
        val user: Int,

        @Min(946684800L)
        @Max(1420070400L)
        @JsonProperty("visited_at")
        val visited_at: Long,

        @Min(0)
        @Max(5)
        @JsonProperty("mark")
        var mark: Int
) : Serializable
