package ru.highloadcup.api

import java.io.Serializable
import java.util.ArrayList

class LocationsDto(val locations: List<Location> = ArrayList()) : Serializable
