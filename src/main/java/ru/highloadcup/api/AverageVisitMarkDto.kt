package ru.highloadcup.api

import java.io.Serializable
import java.math.BigDecimal

class AverageVisitMarkDto(val avg: BigDecimal = BigDecimal.ZERO) : Serializable
