package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

class AverageVisitMarkDto(@JsonProperty("avg") val avg: BigDecimal = BigDecimal.ZERO) : Serializable
